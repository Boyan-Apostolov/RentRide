import { get } from "../baseService";


export const startPinging = () => {
    if (import.meta.env.mode != "production") return;

    ping();
    setInterval(ping, 1000 * 60 * 5);
}

const ping = async () => {
    try {
        const data = await get("cities/test");
        console.log("Ping success:", data);
        return data;
    } catch (error) {
        console.error("Ping failed:", error);
    }
}