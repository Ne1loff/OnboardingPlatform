export type NotificationType = {
    enable: boolean;
    mode: "ONCE" | "EVERY";
    duration: string | null;
    stage: "min" | "hour";
}