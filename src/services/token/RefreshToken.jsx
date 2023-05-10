import configData from "../../config/config.json"

export default async function RefreshToken() {
    const refreshToken = localStorage.getItem("refreshToken");
    let model = {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + refreshToken,
        },
    };

    try {
        const res = await fetch(configData.API_SERVER + 'auth/refresh', model);
        if (!res.ok) {
            throw new Error('');
        }
        const data = await res.json();
        localStorage.setItem("accessToken", data.result);
        return true;
    } catch (error) {
        return false;
    }
}