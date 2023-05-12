import { useDispatch } from "react-redux";
import ModalCheckPW from "../../component/modal/ModalCheckPw";
import { asyncCheckPrivilege } from "../../slice/TokenSlice";

export default function MyPage() {
    const dispatch = useDispatch();
    return (
        <ModalCheckPW
            after={() => {
                dispatch(asyncCheckPrivilege());
            }} />
    )
}