import RefreshToken from "../token/RefreshToken";

export default async function GetUserUsage() {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    const model = {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + accessToken,
        },
    };

    try {
        const res = await fetch(localStorage.getItem("API_SERVER") + 'files/usage', model);
        const data = await res.json();
        if (res.status === 200) {
            const size = parseInt(data.used === null ? 0 : data.used );
            let converted;
            let unit;
            if (parseInt(size / Math.pow(10, 9)) > 0) {
                converted = (size / Math.pow(10, 9)).toFixed(1);
                unit = "GB";
            }
            else if (parseInt(size / Math.pow(10, 6)) > 0) {
                converted = (size / Math.pow(10, 6)).toFixed(1);
                unit = "MB";
            }
            else if (parseInt(size / Math.pow(10, 3)) > 0) {
                converted = (size / Math.pow(10, 3)).toFixed(1);
                unit = "KB";
            }
            else {
                converted = size;
                unit = "B";
            }
            return [true, converted, unit, (size / Math.pow(10, 9)).toFixed(1), data.used];  //성공
        }
        else {
            throw data; //실패
        }
    } catch (e) {
        return [false, e.message]; //실패 후 처리
    }
}