import RefreshToken from "../token/RefreshToken";
 

export default async function GetTrashFileList() {
    await RefreshToken();
    const accessToken = localStorage.getItem("accessToken");
    const model = {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + accessToken,
        },
    };

    try {
        const res = await fetch(localStorage.getItem("API_SERVER") + 'labels/trash', model);
        const data = await res.json();
        const files = [...data.files];
        files.forEach(e => {
            const size = parseInt(e.size);
            if (parseInt(size / Math.pow(10, 9)) > 0) {
                e.size = (size / Math.pow(10, 9)).toFixed(1) + "GB";
            }
            else if (parseInt(size / Math.pow(10, 6)) > 0) {
                e.size = (size / Math.pow(10, 6)).toFixed(1) + "MB";
            }
            else if (parseInt(size / Math.pow(10, 3)) > 0) {
                e.size = (size / Math.pow(10, 3)).toFixed(1) + "KB";
            }
            else {
                e.size = size + "B";
            }
            e.type = "file"
            e.isDeleted = e.labels.find(e => e.name === "!$@*%&Trash") !== undefined;
            e.isShareExist = e.shares.length !== 0;
            e.labels = e.labels.filter(e => e.name.startsWith("!$@*%&") === false);
        });
        const folders = [...data.folders];
        folders.forEach(e => {
            e.type = "folder";
            e.isDeleted = e.labels.find(e => e.name === "!$@*%&Trash") !== undefined;
            e.isShareExist = e.shares.length !== 0;
            e.labels = e.labels.filter(e => e.name.startsWith("!$@*%&") === false);
        });
        
        if (res.status === 200) {
            return [true, [...folders, ...files]];  //성공
        }
        else {
            throw data; //실패
        }
    } catch (e) {
        return [false, e.message]; //실패 후 처리
    }
}