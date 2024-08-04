import type {Static} from "runtypes";
import type {HrRecord} from "./records";

export type HrType = Static<typeof HrRecord>;
export type HrsType = HrType[];