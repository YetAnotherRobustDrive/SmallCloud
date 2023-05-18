import configData from "../../config/config.json"
import RefreshToken from '../token/RefreshToken'

export default async function PostGroup(value) {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    const model = {
        method: "POST",
        headers: {
            "Authorization": "Bearer " + accessToken,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(value)
    };
    // try {
    //     const res = await fetch(configData.API_SERVER + 'auth/elevate', model);
    //     const data = await res.json();
    //     if (!res.ok) {
    //         throw data;
    //     }
    //     localStorage.setItem("accessToken", data.accessToken);
    //     localStorage.setItem("refreshToken", data.refreshToken);
    //     return [true, ''];
    // } catch (data) {
    //     return [false, data.message];
    // }
}