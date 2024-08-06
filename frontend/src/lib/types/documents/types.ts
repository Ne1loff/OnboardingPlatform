import type {Static} from "runtypes";
import type {DocumentRecord} from "./records";

export type DocumentType = Static<typeof DocumentRecord>;
export type DocumentsType = DocumentType[];