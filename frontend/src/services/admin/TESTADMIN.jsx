import configData from "../../config/config.json"
import RefreshToken from '../token/RefreshToken'

export default async function TESTADMIN() {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    let model = {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + accessToken,
        }
    };

    try {
        const res = await fetch(configData.API_SERVER + 'test/admin', model);

        return [true, ''];
    } catch (data) {
        return [false, data.message];
    }
}