 
import RefreshToken from '../token/RefreshToken'

export default async function GetSearchGroup(str) {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    let model = {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + accessToken,
        },
    };

    try {
        const res = await fetch(localStorage.getItem("API_SERVER") + 'group/search?q=' + str, model);
        const data = await res.json();
        if (!res.ok) {
            throw data;
        }
        const withoutRoot = data.filter((d) => d !== "__ROOT__");
        return [true, withoutRoot];
    } catch (data) {
        return [false, data.message];
    }
}