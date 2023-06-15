import React, { useEffect, useState } from "react";
import BodyFrame from "../../component/Bodyframe";
import ExtendBox from "../../component/cs/ExtendBox";
import Header from "../../component/header/Header";
import ModalLoading from "../../component/modal/ModalLoading";
import SidebarCS from "../../component/sidebar/SidebarCS";
import SwalError from "../../component/swal/SwalError";
import GetBoardListFrom from "../../services/board/GetBoardListFrom";

export default function FaqPage() {
    const [dataList, setDataList] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const render = async () => {
            const res = await GetBoardListFrom("inquiries/board?boardType=faq");
            if (!res[0]) {
                SwalError(res[1]);
                return;
            }
            setDataList(res[1]);
            setTimeout(() => setIsLoading(false),250);
        };
        render();
    }, [])

    return (
        <>
            {isLoading && <ModalLoading isOpen={isLoading} />}
            <Header />
            <SidebarCS />
            <BodyFrame>
                {dataList.length === 0 &&
                    <div className="login" style={{ paddingTop: "10%" }}>등록된 FAQ가 없습니다.</div>
                }
                {dataList.length !== 0 &&
                    dataList.map((data) => {
                        return <ExtendBox key={data.id} title={data.title}>{data.content}</ExtendBox>
                    })
                }
            </BodyFrame>
        </>
    )
}