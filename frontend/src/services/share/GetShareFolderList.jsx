import RefreshToken from "../token/RefreshToken";
 

export default async function GetShareFolderList() {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    const model = {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + accessToken,
        },
    };

    try {
        const res = await fetch(localStorage.getItem("API_SERVER") + 'share/directory-list', model);
        const data = await res.json();
        data.forEach(e => {
            e.type = "folder";
            e.isFavorite = e.labels.find(e => e.name === "!$@*%&Favorite") !== undefined;
            e.labels = e.labels.filter(e => e.name.startsWith("!$@*%&") === false);
        });
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