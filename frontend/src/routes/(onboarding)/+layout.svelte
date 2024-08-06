<script lang="ts">
    import { pushState } from "$app/navigation";
    import { navigating, page } from "$app/stores";
    import { ActionFlowNodeType } from "$lib/scenaries/scenarios.flow";
    import type { ScenariosType } from "$lib/scenaries/types/scenarios.types";
    import {
        Content,
        Header,
        HeaderGlobalAction,
        HeaderNav,
        HeaderNavItem,
        HeaderUtilities,
        Loading,
        SideNav,
        SideNavDivider,
        SideNavItems,
        SideNavLink,
        SkipToContent,
    } from "carbon-components-svelte";
    import "carbon-components-svelte/css/white.css";
    import {
        Attachment,
        ChangeCatalog,
        DirectionRight_01,
        Send,
        Settings,
        UserMultiple,
        WatsonHealthTextAnnotationToggle ,
    } from "carbon-icons-svelte";
    import { setContext } from "svelte";
    import { type Writable, writable } from "svelte/store";

    const currentScenarios: Writable<ScenariosType> = writable();
    setContext("currentScenarios", currentScenarios);

    let isSideNavOpen = false;

    const onDragStart = (event: DragEvent, nodeType: string) => {
        if (!event.dataTransfer) {
            return null;
        }

        event.dataTransfer.setData("application/svelteflow", nodeType);
        event.dataTransfer.effectAllowed = "move";
    };

    function showSettingsModal(event: MouseEvent) {
        event.preventDefault();

        pushState(`${$page.url.pathname}/settings`, {
            showSettings: true,
        });
    }

    let renderScenariosHelpers = false;
    $: renderScenariosHelpers =
        $page.route.id?.includes("/scenaries/[scenarios]") ?? false;
</script>

{#if $navigating}
    <Loading />
{:else}
    <Header company="Кей Системс" platformName="Платформа" bind:isSideNavOpen>
        <svelte:fragment slot="skip-to-content">
            <SkipToContent />
        </svelte:fragment>
        <HeaderNav>
            <HeaderNavItem href="/scenaries" text="Сценарии" />
            <HeaderNavItem href="/documents" text="Документы" />
        </HeaderNav>
        {#if renderScenariosHelpers}
            <HeaderUtilities>
                <HeaderGlobalAction
                    tooltipAlignment="end"
                    iconDescription="Settings"
                    icon={Settings}
                    href={`${$page.url.pathname}/settings`}
                    on:click={showSettingsModal}
                />
            </HeaderUtilities>
        {/if}
    </Header>
    {#if renderScenariosHelpers}
        <SideNav bind:isOpen={isSideNavOpen} rail>
            <!-- svelte-ignore a11y-no-static-element-interactions -->
            <SideNavItems>
                <SideNavLink icon={Send} text="Отправить сообщение">
                    <div
                        on:dragstart={(event) =>
                            onDragStart(event, ActionFlowNodeType.SEND_MESSAGE)}
                        draggable={true}
                    >
                        Отправить сообщение
                    </div>
                </SideNavLink>
                <SideNavLink icon={Attachment} text="Отправить файл">
                    <div
                        on:dragstart={(event) =>
                            onDragStart(event, ActionFlowNodeType.SEND_FILE)}
                        draggable={true}
                    >
                        Отправить файл
                    </div>
                </SideNavLink>
                <SideNavLink icon={UserMultiple} text="Отправить контакт">
                    <div
                        on:dragstart={(event) =>
                            onDragStart(event, ActionFlowNodeType.SEND_CONTACT)}
                        draggable={true}
                    >
                        Отправить контакт
                    </div>
                </SideNavLink>
                <SideNavLink
                    icon={WatsonHealthTextAnnotationToggle }
                    text="Записать ответ"
                >
                    <div
                        on:dragstart={(event) =>
                            onDragStart(event, ActionFlowNodeType.READ_MESSAGE)}
                        draggable={true}
                    >
                        Записать ответ
                    </div>
                </SideNavLink>
                <SideNavLink
                    icon={DirectionRight_01}
                    text="Переслать сообщение"
                >
                    <div
                        on:dragstart={(event) =>
                            onDragStart(
                                event,
                                ActionFlowNodeType.FORWARD_MESSAGE,
                            )}
                        draggable={true}
                    >
                        Переслать сообщение
                    </div>
                </SideNavLink>
                <SideNavLink icon={ChangeCatalog} text="Изменить сценарий">
                    <div
                        on:dragstart={(event) =>
                            onDragStart(
                                event,
                                ActionFlowNodeType.CHANGE_SCENARIOS,
                            )}
                        draggable={true}
                    >
                        Изменить сценарий
                    </div>
                </SideNavLink>
                <SideNavDivider />
                <SideNavLink
                    icon={Settings}
                    text="Настройки"
                    href={`${$page.url.pathname}/settings`}
                    on:click={showSettingsModal}
                />
            </SideNavItems>
        </SideNav>
    {/if}

    <Content style="height: calc(100vh - 3rem)">
        <slot />
    </Content>
{/if}
