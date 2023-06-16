import RefreshToken from "../token/RefreshToken";

export default async function GetConfig(code) {
    const model = {
        method: "GET",
    };

    try {
        const res = await fetch(localStorage.getItem("API_SERVER") + "admin/config/" + code , model);
        if (res.status === 200) {
            return [true, ''];  //성공
        }
        else {
            const data = await res.json();
            throw data; //실패
        }
    } catch (e) {
        return [false, e.message]; //실패 후 처리
    }
}