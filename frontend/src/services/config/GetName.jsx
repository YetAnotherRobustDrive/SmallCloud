
export default async function GetName() {
    const model = {
        method: "GET",
    };

    try {
        const res = await fetch(localStorage.getItem("API_SERVER") + "admin/config/logo", model);
        if (res.status === 200) {
            if (res.body === null) {
                return "S-Cloud";
            }
            const data = await res.json();
            return data.value;  //성공
        }
        else {
            return "S-Cloud"; //실패
        }
    } catch (e) {
        return "S-Cloud";
    }
}