<script lang="ts">
    import { ActionFlowNodeType } from "$lib/scenaries/scenarios.flow";
    import {
        Content,
        Header,
        HeaderNav,
        HeaderNavItem,
        HeaderNavMenu,
        SideNav,
        SideNavDivider,
        SideNavItems,
        SideNavLink,
        SideNavMenu,
        SideNavMenuItem,
        SkipToContent,
    } from "carbon-components-svelte";
    import "carbon-components-svelte/css/white.css";
    import { AppConnectivity, Settings } from "carbon-icons-svelte";
    const scenaries = [
        { href: "/", name: "Онбординг" },
        { href: "/", name: "Валюта" },
    ];

    let isSideNavOpen = false;

    const onDragStart = (event: DragEvent, nodeType: string) => {
        if (!event.dataTransfer) {
            return null;
        }

        event.dataTransfer.setData("application/svelteflow", nodeType);
        event.dataTransfer.effectAllowed = "move";
    };
</script>

<Header company="Кей Системс" platformName="Платформа" bind:isSideNavOpen>
    <svelte:fragment slot="skip-to-content">
        <SkipToContent />
    </svelte:fragment>
    <HeaderNav>
        <HeaderNavItem href="/projects" text="Проекты" />
        <HeaderNavMenu text="Сценарии">
            {#each scenaries as scenarios}
                <HeaderNavItem href={scenarios.href} text={scenarios.name} />
            {/each}
        </HeaderNavMenu>
        <HeaderNavItem href="/knowledges" text="База знаний" />
        <HeaderNavItem href="/documents" text="Документы" />
    </HeaderNav>
</Header>

<SideNav bind:isOpen={isSideNavOpen} rail>
    <SideNavItems>
        <!-- svelte-ignore a11y-no-static-element-interactions -->
        <SideNavMenu icon={AppConnectivity} text="Действия">
            <SideNavMenuItem text="Отправить сообщение">
                <div
                    on:dragstart={(event) =>
                        onDragStart(event, ActionFlowNodeType.SEND_MESSAGE)}
                    draggable={true}
                >
                    Отправить сообщение
                </div>
            </SideNavMenuItem>
            <SideNavMenuItem text="Отправить файл">
                <div
                    on:dragstart={(event) =>
                        onDragStart(event, ActionFlowNodeType.SEND_FILE)}
                    draggable={true}
                >
                    Отправить файл
                </div>
            </SideNavMenuItem>
            <SideNavMenuItem text="Отправить контакт">
                <div
                    on:dragstart={(event) =>
                        onDragStart(event, ActionFlowNodeType.SEND_CONTACT)}
                    draggable={true}
                >
                    Отправить контакт
                </div>
            </SideNavMenuItem>
            <SideNavMenuItem text="Прочитать сообщение">
                <div
                    on:dragstart={(event) =>
                        onDragStart(event, ActionFlowNodeType.READ_MESSAGE)}
                    draggable={true}
                >
                    Прочитать сообщение
                </div>
            </SideNavMenuItem>
            <SideNavMenuItem text="Переслать сообщение">
                <div
                    on:dragstart={(event) =>
                        onDragStart(event, ActionFlowNodeType.FORWARD_MESSAGE)}
                    draggable={true}
                >
                    Переслать сообщение
                </div>
            </SideNavMenuItem>
            <SideNavMenuItem text="Изменить сценарий">
                <div
                    on:dragstart={(event) =>
                        onDragStart(event, ActionFlowNodeType.CHANGE_SCENARIOS)}
                    draggable={true}
                >
                    Изменить сценарий
                </div>
            </SideNavMenuItem>
        </SideNavMenu>
        <SideNavDivider />
        <SideNavLink icon={Settings} text="Настройки" href="/settings" />
    </SideNavItems>
</SideNav>

<Content>
    <slot />
</Content>
