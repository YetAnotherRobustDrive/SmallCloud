import configData from "../config/config.json"

export default async function RefreshToken() {
    const refreshToken = localStorage.getItem("refreshToken");
    console.log(refreshToken);
    let model = {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + refreshToken,
        },
    };

    try {
        const res = await fetch(configData.API_SERVER + 'auth/refresh', model);
        console.log(res);
        if (!res.ok) {
            throw new Error('');
        }
        else {
            const token = await res.text();
            localStorage.setItem("accessToken", token);
            return true;
        }        
    } catch (error) {
        return false;
    }
}