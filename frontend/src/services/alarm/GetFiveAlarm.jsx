import RefreshToken from "../token/RefreshToken";
import configData from "../../config/config.json";
import jwtDecode from "jwt-decode";

export default async function GetFiveAlarm() {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    const model = {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + accessToken,
        },
    };

    try {
        const username = jwtDecode(localStorage.getItem("accessToken")).sub;
        if (username === "root") {
            return [true, { notificationDtoList: [], count: 0 }];
        }
        const res = await fetch(configData.API_SERVER + 'notifications/' + username + '/confirm', model);
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