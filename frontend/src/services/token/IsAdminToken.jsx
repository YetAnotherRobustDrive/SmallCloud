 
import RefreshToken from "./RefreshToken";

export default async function IsAdminToken() {
    await RefreshToken();

    try {
        const accessToken = localStorage.getItem("accessToken");
        const res = await fetch(localStorage.getItem("API_SERVER") + 'auth/admin-check', {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + accessToken,
            },
        })
        const data = await res.json();
        return data.result;
    } catch (error) {
        return false;
    }
}