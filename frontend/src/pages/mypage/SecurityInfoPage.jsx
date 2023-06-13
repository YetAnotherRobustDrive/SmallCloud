import React, { useEffect, useState } from "react";
import Header from "../../component/header/Header";
import SidebarMypage from "../../component/sidebar/SidebarMypage";
import BodyFrame from "../../component/Bodyframe";
import BodyHeader from "../../component/main/BodyHeader";
import GetLoginLog from "../../services/log/GetLoginLog";

export default function SecurityInfoPage() {

    const [log, setLog] = useState([]);

    useEffect(() => {
        const init = async () => {
            const res = await GetLoginLog();
            setLog(res[1]);
        }
        init();
    }, []);

    return (
        <>
            <Header />
            <SidebarMypage />
            <BodyFrame>
                <BodyHeader text="로그인 내역" />
                <div className="logTable">
                    <table>
                        <thead>
                            <tr>
                                <th>시간</th>
                                <th>종류</th>
                                <th>결과</th>
                                <th>IP</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                log.map((item, index) => (
                                    <tr key={index}>
                                        <td>{item.localDateTime}</td>
                                        <td>{item.action}</td>
                                        <td>{item.status===true ? "성공" : "실패"}</td>
                                        <td>{item.ipAddr}</td>
                                    </tr>
                                ))
                            }
                        </tbody>
                    </table>
                </div>
            </BodyFrame>
        </>
    )
}
