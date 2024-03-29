 
import RefreshToken from '../token/RefreshToken'

export default async function ElevateUser(pw) {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    let model = {
        method: "POST",
        headers: {
            "Authorization": "Bearer " + accessToken,
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            "password": pw, 
        })
    };

    try {
        const res = await fetch(localStorage.getItem("API_SERVER") + 'auth/elevate', model);
        const data = await res.json();
        if (!res.ok) {
            throw data;
        }
        localStorage.setItem("accessToken", data.accessToken);
        localStorage.setItem("refreshToken", data.refreshToken);
        return [true, ''];
    } catch (data) {
        return [false, data.message];
    }
}