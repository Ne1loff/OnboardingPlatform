<script lang="ts">
    import type { SendContactActionType } from "$lib/scenaries/types/scenarios.types";
    import type { HrsType, HrType } from "$lib/types/hrs/types";
    import {
        ComboBox,
        ContentSwitcher,
        FormGroup,
        InlineNotification,
        Switch,
        TextInput,
    } from "carbon-components-svelte";
    import type { ComboBoxItem } from "carbon-components-svelte/src/ComboBox/ComboBox.svelte";
    import IMask, { type InputMask } from "imask";
    import { getContext, onDestroy } from "svelte";
    import { derived, type Writable } from "svelte/store";

    export let action: Writable<SendContactActionType>;

    const hrs = getContext<Writable<HrsType>>("hrs");
    const hrsItems = derived<Writable<HrsType>, ComboBoxItem[]>(hrs, ($hrs) =>
        $hrs.map((it) => ({ id: it, text: telegramUserNameMapper(it) })),
    );

    function telegramUserNameMapper(hr: HrType): string {
        let nameInfo = "";

        if (hr.firstName) nameInfo += ` (${hr.firstName}`;
        if (hr.firstName && hr.lastName) nameInfo += ` ${hr.lastName}`;
        if (hr.firstName) nameInfo += ")";

        return `@${hr.username}${nameInfo}`;
    }

    function shouldFilterItem(item: ComboBoxItem, value: string): boolean {
        if (!value) return true;

        const text = item.text.toLowerCase();
        const comparison = value.toLowerCase();

        return text.includes(comparison);
    }

    function setContact(selectedContact: HrType | null) {
        if (!selectedContact) return;

        $action.firstName = selectedContact.firstName ?? "";
        $action.lastName = selectedContact.lastName ?? "";
    }

    let selectedContact = $hrsItems.find(
        (it) =>
            (<HrType>it.id).firstName === $action.firstName &&
            (<HrType>it.id).lastName === $action.lastName,
    )?.id;
    let selectedIndex = 0;

    let numberInput: HTMLInputElement;
    let mask: InputMask<any>;

    function createMaskFor(elem: HTMLInputElement) {
        if (!numberInput) return;

        const maskOptions = {
            mask: "+{7}(000)000-00-00",
        };

        if (mask) mask.destroy();
        mask = IMask(elem, maskOptions);
    }

    $: setContact(selectedContact);
    $: createMaskFor(numberInput);
    onDestroy(() => {
        if (mask) mask.destroy();
    });
</script>

<div class="container">
    <FormGroup noMargin>
        <InlineNotification
            hideCloseButton
            lowContrast
            kind="info-square"
            title="Важно:"
            subtitle="Данное действие нужно для отправки контакта. Для отправки Telegram профиля, лучше воспользоваться действием: [Отправить сообщение] и добавить ссылку на профиль."
        />
    </FormGroup>
    <FormGroup legendText="Файлы для отправки" noMargin>
        <ContentSwitcher bind:selectedIndex>
            <Switch text="Выбрать из HR-специалистов" />
            <Switch text="Ввести другой" />
        </ContentSwitcher>
    </FormGroup>
    <FormGroup>
        {#if selectedIndex === 0}
            <ComboBox
                titleText="Контакт для отправки"
                placeholder="Пользователь не выбран"
                direction="top"
                invalid={selectedContact === null}
                invalidText="Должен быть выбран пользователь, контакт которого будет отправлен"
                selectedId={selectedContact}
                items={$hrsItems}
                {shouldFilterItem}
                on:select={({ detail }) =>
                    (selectedContact = detail.selectedId)}
                on:clear={() => (selectedContact = null)}
            />
            <TextInput
                bind:ref={numberInput}
                labelText="Номер телефона"
                placeholder="Введите номер..."
                bind:value={$action.phoneNumber}
            />
        {:else}
            <TextInput
                bind:ref={numberInput}
                labelText="Номер телефона"
                placeholder="Введите номер..."
                bind:value={$action.phoneNumber}
            />
            <TextInput
                labelText="Имя"
                placeholder="Введите имя..."
                bind:value={$action.firstName}
            />
            <TextInput
                labelText="Фамилия"
                placeholder="Введите фамилию..."
                bind:value={$action.lastName}
            />
        {/if}
    </FormGroup>
</div>

<style>
    .container {
        margin: 1rem 0;
    }
</style>
