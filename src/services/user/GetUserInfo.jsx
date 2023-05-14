import RefreshToken from "../token/RefreshToken";

export default async function asyncGetUserInfo() {
    await RefreshToken();

    try {
        //const accessToken = localStorage.getItem("accessToken");
        // const res = await fetch(configData.API_SERVER + 'auth/privileged', {
        //     method: "GET",
        //     headers: {
        //         "Authorization": "Bearer " + accessToken,
        //     },
        // })
        // const data = await res.json();
        return "test";
    } catch (error) {
        return false;
    }
}