import { useDispatch } from "react-redux";
import ModalCheckPW from "../../component/modal/ModalCheckPw";
import { asyncSetPrivilege } from "../../slice/UserSlice";

export default function MyPage() {
    const dispatch = useDispatch();
    return (
        <ModalCheckPW
            after={() => {
                dispatch(asyncSetPrivilege());
            }} />
    )
}