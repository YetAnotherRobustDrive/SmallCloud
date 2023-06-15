 
import RefreshToken from '../token/RefreshToken'

export default async function GetSearchUser(str) {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    let model = {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + accessToken,
        },
    };

    try {
        const res = await fetch(localStorage.getItem("API_SERVER") + 'users/search?q=' + str, model);
        const data = await res.json();
        if (!res.ok) {
            throw data;
        }
        return [true, data.result];
    } catch (data) {
        return [false, data.message];
    }
}