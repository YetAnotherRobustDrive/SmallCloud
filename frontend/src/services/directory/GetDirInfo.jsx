import RefreshToken from "../token/RefreshToken";

export default async function GetDirInfo(id) {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    const model = {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + accessToken,
        },
    };

    try {
        const res = await fetch(localStorage.getItem("API_SERVER") + 'directory/' + id , model);
        const data = await res.json();
        if (res.status === 200) {
            return [true, data];  //성공
        }
        else {
            throw data; //실패
        }
    } catch (e) {
        return [false, e.message]; //실패 후 처리
    }
}