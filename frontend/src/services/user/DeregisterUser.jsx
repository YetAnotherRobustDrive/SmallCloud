import configData from "../../config/config.json"

export default async function DeregisterUser() {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    let model = {
        method: "POST",
        headers: {
            "Authorization": "Bearer " + accessToken,
        },
    };

    try {
        const res = await fetch(configData.API_SERVER + 'auth/deregister', model);
        if (!res.ok) {
            const data = await res.json();
            throw data;
        }
        const token = await res.text();
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        return [true, ''];
    } catch (data) {
        return [false, data.message];
    }
}