/*
 * Copyright 2010-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.resolve.calls.smartcasts

import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.descriptors.PackageViewDescriptor
import org.jetbrains.kotlin.descriptors.VariableDescriptor
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.calls.smartcasts.DataFlowValue.Kind.OTHER
import org.jetbrains.kotlin.resolve.calls.smartcasts.DataFlowValue.Kind.STABLE_VALUE
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.resolve.scopes.receivers.ImplicitReceiver
import org.jetbrains.kotlin.resolve.scopes.receivers.ReceiverValue
import org.jetbrains.kotlin.types.KotlinType

interface IdentifierInfo {

    val kind: DataFlowValue.Kind get() = OTHER

    object NO : IdentifierInfo {
        override fun toString() = "NO_IDENTIFIER_INFO"
    }

    object NULL : IdentifierInfo {
        override fun toString() = "NULL"
    }

    object ERROR : IdentifierInfo {
        override fun toString() = "ERROR"
    }

    class Variable(val variable: VariableDescriptor, override val kind: DataFlowValue.Kind) : IdentifierInfo {

        override fun equals(other: Any?) =
                other is Variable && variable == other.variable && kind.isStable() == other.kind.isStable()

        override fun hashCode() = variable.hashCode() + 31 * (if (kind.isStable()) 1 else 0)

        override fun toString() = variable.toString()
    }

    data class Receiver(val value: ReceiverValue) : IdentifierInfo {

        override val kind = STABLE_VALUE

        override fun toString() = value.toString()
    }

    data class PackageOrClass(val descriptor: DeclarationDescriptor) : IdentifierInfo {

        override val kind = STABLE_VALUE

        override fun toString() = descriptor.toString()
    }

    class Qualified(
            val receiverInfo: IdentifierInfo,
            val selectorInfo: IdentifierInfo,
            val safe: Boolean,
            val receiverType: KotlinType?
    ) : IdentifierInfo {
        override val kind: DataFlowValue.Kind get() = if (receiverInfo.kind.isStable()) selectorInfo.kind else OTHER

        override fun equals(other: Any?) = other is Qualified && receiverInfo == other.receiverInfo && selectorInfo == other.selectorInfo

        override fun hashCode() = 31 * receiverInfo.hashCode() + selectorInfo.hashCode()

        override fun toString() = "$receiverInfo${if (safe) "?." else "."}$selectorInfo"
    }

    companion object {

        fun qualified(
                receiverInfo: IdentifierInfo,
                receiverType: KotlinType?,
                selectorInfo: IdentifierInfo,
                safe: Boolean
        ) = when (receiverInfo) {
            NO -> NO
            is PackageOrClass -> selectorInfo
            else -> Qualified(receiverInfo, selectorInfo, safe, receiverType)
        }
    }
}