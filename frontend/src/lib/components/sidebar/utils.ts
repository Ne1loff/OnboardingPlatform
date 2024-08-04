import {ActionFlowNodeType, CORE_ACTION_FLOW_NODE_TYPES} from "$lib/scenaries/scenarios.flow";
import type {ActionFlow} from "$lib/scenaries/types/scenarios.node.types";
import type {ActionsType} from "$lib/scenaries/types/scenarios.types";
import {ActionLinkRecord} from "$lib/scenaries/types/scenarios.types.records";
import {type Node,} from "@xyflow/svelte";
import type {DropdownItem} from "carbon-components-svelte/src/Dropdown/Dropdown.svelte";

export const getActionName = (data: ActionFlow<ActionsType>): string => {
    let name: string = "";
    const unsubscribe = data.flow.subscribe((it) => {
        name = ActionLinkRecord.guard(it) ? `Ссылка: ${it.name}` : it.name
    });
    unsubscribe();

    return name;
};

export const getSelectableActions = (
    it: Node<Record<string, unknown>, string>[],
    defaultItem: { id: string, text: string } | undefined = undefined
): DropdownItem[] => {
    const items: DropdownItem[] = it
        .filter((it) =>
            CORE_ACTION_FLOW_NODE_TYPES.includes(
                <ActionFlowNodeType>it.type,
            ),
        )
        .map((it) => ({
            id: it.id,
            text: getActionName(<ActionFlow<ActionsType>>it.data),
        }));

    if (defaultItem) items.unshift(defaultItem);

    return items;
};