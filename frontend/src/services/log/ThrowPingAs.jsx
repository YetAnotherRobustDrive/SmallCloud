export default async function ThrowPingAs(message) {
    const model = {
        method: "GET",
    };

    try {
        const res = await fetch(localStorage.getItem("API_SERVER") + 'ping/' + message, model);
        if (res.status === 200) {
            return [true, ''];  //성공
        }
        else {
            return [true, ''];  //성공
        }
    } catch (e) {
        return [false, e.message];  //성공
    }
}