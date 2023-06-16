import RefreshToken from "../token/RefreshToken";

export default async function AdminPostConfig(code, value) {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    const model = {
        method: "POST",
        headers: {
            "Authorization": "Bearer " + accessToken,
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ 
            "code" : code,
            "value" : value
        })
    };
    try {
        const res = await fetch(localStorage.getItem("API_SERVER") + "admin/config" , model);
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