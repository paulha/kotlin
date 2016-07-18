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

@file:JvmName("KTypes")
package kotlin.reflect

import org.jetbrains.kotlin.types.TypeUtils
import kotlin.reflect.jvm.internal.KTypeImpl

/**
 * Returns a new type with the same classifier, arguments and nullability as the given type, and annotated with the given annotations.
 * Annotations on the original type are *discarded*.
 */
fun KType.withAnnotations(annotations: List<Annotation>): KType {
    // TODO (!)
    TODO()
}

/**
 * Returns a new type with the same classifier, arguments and annotations as the given type, and with the given nullability.
 */
fun KType.withNullability(nullable: Boolean): KType {
    return if (isMarkedNullable == nullable) this
    else KTypeImpl(TypeUtils.makeNullableAsSpecified((this as KTypeImpl).type, nullable), { javaType })
}
