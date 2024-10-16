/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ca.gosyer.jui.domain.backup.service

import ca.gosyer.jui.core.io.SYSTEM
import ca.gosyer.jui.domain.backup.model.BackupValidationResult
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Multipart
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Part
import de.jensklingenberg.ktorfit.http.ReqBuilder
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.formData
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.content.PartData
import kotlinx.coroutines.flow.Flow
import okio.FileSystem
import okio.Path
import okio.buffer

interface BackupRepositoryOld {
    @Multipart
    @POST("api/v1/backup/import/file")
    fun importBackupFile(
        @Part("") formData: List<PartData>,
        @ReqBuilder block: HttpRequestBuilder.() -> Unit = {},
    ): Flow<HttpResponse>

    @Multipart
    @POST("api/v1/backup/validate/file")
    fun validateBackupFile(
        @Part("") formData: List<PartData>,
        @ReqBuilder block: HttpRequestBuilder.() -> Unit = {},
    ): Flow<BackupValidationResult>

    @GET("api/v1/backup/export/file")
    fun exportBackupFile(
        @ReqBuilder block: HttpRequestBuilder.() -> Unit = {},
    ): Flow<HttpResponse>

    companion object {
        fun buildBackupFormData(file: Path) =
            formData {
                append(
                    "backup.proto.gz",
                    FileSystem.SYSTEM.source(file).buffer().readByteArray(),
                    Headers.build {
                        append(HttpHeaders.ContentType, ContentType.MultiPart.FormData.toString())
                        append(HttpHeaders.ContentDisposition, "filename=backup.proto.gz")
                    },
                )
            }
    }
}
