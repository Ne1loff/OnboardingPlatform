<script lang="ts">
    import { goto, invalidate } from "$app/navigation";

    import {
        Button,
        Column,
        ComposedModal,
        FileUploaderButton,
        FileUploaderDropContainer,
        FileUploaderItem,
        Grid,
        InlineNotification,
        ModalBody,
        ModalFooter,
        ModalHeader,
        Row,
    } from "carbon-components-svelte";
    import TrashCan from "carbon-icons-svelte/lib/TrashCan.svelte";
    import { writable } from "svelte/store";

    type UploadingStatus = "uploading" | "edit" | "complete" | "error";
    type FileStatus = {
        errorSubject?: string;
        errorBody?: string;
        isInvalid: boolean;
    };
    const limit2GB = 2n << 30n;
    const limit10GB = 10n << 30n;

    const files = writable<File[]>([]);
    const uploadingStatus = writable<UploadingStatus>("edit");
    const uploadingIsDone = writable<boolean>(false);

    function resetHelpers(_: boolean) {
        uploadingStatus.set("edit");
        uploadingIsDone.set(false);
    }

    function close() {
        goto("/documents");
    }

    async function submit() {
        const data = new FormData();
        $files.forEach((it) => data.append("files", it, it.name));

        $uploadingStatus = "uploading";

        const response = await fetch("/api/v1/documents/upload", {
            method: "POST",
            body: data,
        });
        $uploadingIsDone = true;
        
        if (response.status === 200) {
            $uploadingStatus = "complete";
            invalidate("app:documents");
            return;
        }

        $uploadingStatus = "error";
        console.error(response);
    }

    function deleteFile(name: string) {
        files.update((old) => old.filter((it) => it.name !== name));
        clearDummies();
    }

    function addFiles(newFiles: readonly File[]) {
        if (newFiles.length === 0) return;

        files.update((old) => {
            old.push(...newFiles);
            return old;
        });

        clearDummies();
    }

    function clearFiles() {
        files.set([]);
        clearDummies();
    }

    function clearDummies() {
        containerFiles = [];
        buttonFiles = [];
    }

    let containerFiles: File[];
    let buttonFiles: File[];
    const chunkSize = 2;

    function calculateChunks(files: File[]): File[][] {
        const result: File[][] = [];

        for (let i = 0; i < files.length; i += chunkSize) {
            const chunk = files.slice(i, i + chunkSize);
            result.push(chunk);
        }

        return result;
    }

    function getFileStatus(file: File): FileStatus {
        const isSizeLimit = file.size >= limit2GB;
        const errorSubject = isSizeLimit
            ? "Размер файла превышает ограничение в 2 ГБ"
            : undefined;
        const errorBody = isSizeLimit
            ? "Пожалуйста, выберите другой файл"
            : undefined;

        return {
            errorSubject,
            errorBody,
            isInvalid: isSizeLimit,
        };
    }

    function calculateUploadLimits(files: File[]): [boolean, boolean] {
        let hasUploadSizeLimit = false;
        let hasFileSizeLimit = false;

        let filesSizeSum = 0n;
        for (const file of files) {
            const fileSize = BigInt(file.size);
            filesSizeSum += fileSize;

            if (fileSize >= limit2GB) {
                hasFileSizeLimit = true;
            }
        }
        hasUploadSizeLimit = filesSizeSum >= limit10GB;

        return [hasUploadSizeLimit, hasFileSizeLimit];
    }

    $: hasUploadedFiles = $files.length > 0;
    $: resetHelpers(hasUploadedFiles);
    $: chunkedFiles = calculateChunks($files);
    $: [isUploadSizeLimit, isFileSizeLimit] = calculateUploadLimits($files);
    $: primaryButtonIsDisabled = !$uploadingIsDone && (!hasUploadedFiles || $uploadingStatus !== "edit" ||isUploadSizeLimit || isFileSizeLimit);
</script>

<svelte:head>
    <title>Загрузка файлов</title>
</svelte:head>
<ComposedModal open={true} on:close={close} on:submit={() => $uploadingIsDone ? close() : submit()}>
    <ModalHeader
        label="Загрузка файлов"
        title="Добавте файлы для загрузки в систему"
    />
    <ModalBody id="uploader-modal">
        <FileUploaderDropContainer
            bind:files={containerFiles}
            multiple
            labelText="Перетащите файлы сюда или нажмите, чтобы выбрать для загрузки"
            on:add={({ detail }) => addFiles(detail)}
            disabled={isUploadSizeLimit || $uploadingIsDone}
        />
        <Grid fullWidth noGutter>
            <Row padding={!isUploadSizeLimit && hasUploadedFiles}>
                <Column noGutterRight={hasUploadedFiles}>
                    <FileUploaderButton
                        bind:files={buttonFiles}
                        multiple
                        size="default"
                        labelText="Выбрать файлы"
                        on:change={({ detail }) => addFiles(detail)}
                        disabled={isUploadSizeLimit || $uploadingIsDone}
                    />
                </Column>
                {#if hasUploadedFiles}
                    <Column noGutterLeft={hasUploadedFiles}>
                        <Button
                            kind="danger-tertiary"
                            on:click={clearFiles}
                            icon={TrashCan}>Удалить все файлы</Button
                        >
                    </Column>
                {/if}
            </Row>
            {#if isUploadSizeLimit}
                <Row>
                    <Column>
                        <InlineNotification
                            id="upload-limit-notifier"
                            hideCloseButton
                            kind="error"
                            title="Превышен лимит загрузки:"
                            subtitle="Лимит загрузки файлов - 10 ГБ"
                        />
                    </Column>
                </Row>
            {/if}
            {#each chunkedFiles as chunk}
                <Row>
                    {#each chunk as file, index (file.name)}
                        <Column
                            noGutterRight={index === 0 && chunk.length > 1}
                            noGutterLeft={index === 1}
                        >
                            {@const fileStatus = getFileStatus(file)}
                            <FileUploaderItem
                                errorSubject={fileStatus.errorSubject}
                                errorBody={fileStatus.errorBody}
                                invalid={fileStatus.isInvalid ||
                                    $uploadingStatus === "error"}
                                status={$uploadingStatus === "error"
                                    ? "edit"
                                    : $uploadingStatus}
                                name={file.name}
                                id="file-item"
                                on:delete={() => deleteFile(file.name)}
                            />
                        </Column>
                    {/each}
                </Row>
            {/each}
        </Grid>
    </ModalBody>
    <ModalFooter
        primaryButtonText={$uploadingIsDone ? "Закрыть" : "Загрузить"}
        primaryButtonDisabled={primaryButtonIsDisabled}
        secondaryButtonText="Отмена"
        on:click:button--secondary={close}
    />
</ComposedModal>

<style>
    :global(
            #uploader-modal .bx--file-browse-btn,
            #uploader-modal #upload-limit-notifier
        ) {
        max-width: unset;
    }

    :global(#uploader-modal .bx--btn) {
        max-width: unset;
        width: 100%;
    }

    :global(#uploader-modal .bx--file__selected-file) {
        max-width: unset;
        width: 100%;
    }

    :global(#uploader-modal #file-item) {
        border: 2px solid #0f62fe;
    }
</style>
