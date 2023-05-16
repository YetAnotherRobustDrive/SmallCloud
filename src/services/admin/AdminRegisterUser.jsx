import RefreshToken from "../token/RefreshToken";
import configData from "../../config/config.json"

export default async function AdminRegisterUser(value) {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    const model = {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + accessToken,
        },
        body: JSON.stringify(value),
    };

    try {
        const res = await fetch(configData.API_SERVER + 'users', model);
        if (res.status == 200) {
            return [true, ''];  //성공
        }
        else if (res.status == 400) {
            const data = await res.json();
            throw "입력 형식 오류입니다.";
        }
        else {
            const data = await res.json();
            throw data.message; //실패
        }
    } catch (message) {
        return [false, message]; //실패 후 처리
    }
}