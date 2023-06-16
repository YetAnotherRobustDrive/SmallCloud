import RefreshToken from "../token/RefreshToken";

export default async function GetConfig(code) {
    const model = {
        method: "GET",
    };

    try {
        const res = await fetch(localStorage.getItem("API_SERVER") + "admin/config/" + code, model);
        if (res.status === 200) {
            if (res.body === null) {
                return [true, null];
            }
            const data = await res.json();
            return [true, data.value];  //성공
        }
        else {
            const data = await res.json();
            return [false, data.message]; //실패
        }
    } catch (e) {
        return [true, null];
    }
}