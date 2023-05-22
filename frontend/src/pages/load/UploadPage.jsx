import React from "react";
import BodyFrame from "../../component/Bodyframe";
import Header from "../../component/header/Header";
import Sidebar from "../../component/sidebar/Sidebar";
import LoadListBox from "../../component/updown/LoadListBox";
import ProgressBar from "../../component/updown/ProgressBar";
import '../../css/load.css'

export default function UploadPage(){

    const [progress, setProgress] = React.useState(10);

    React.useEffect(() => {
      const timer = setInterval(() => {
        setProgress((prevProgress) => (prevProgress >= 100 ? 10 : prevProgress + 10));
      }, 800);
      return () => {
        clearInterval(timer);
      };
    }, []);

    return (
        <>
            <Header/>
            <Sidebar/>
            <BodyFrame>
                <LoadListBox title="현재 업로드 중인 파일">
                    <ProgressBar 
                    name="test.txt"
                    value={progress}/>
                </LoadListBox>
                <LoadListBox title="업로드 예정 파일 목록">
                    <div>test</div>
                    <div>test</div>
                    <div>test</div>
                    <div>test</div>
                    <div>test</div>
                    <div>test</div>
                    <div>test</div>
                    <div>test</div>
                    <div>test</div>
                    <div>test</div>
                    <div>test</div>
                    <div>test</div>
                    <div>test</div>
                    <div>test</div>
                    <div>test</div>
                    <div>test</div>
                </LoadListBox>
                <LoadListBox title="업로드 완료 파일 목록">
                    <div>test</div>
                    <div>test</div>
                    <div>test</div>
                    <div>test</div>
                    <div>test</div>
                    <div>test</div>
                    <div>test</div>
                    <div>test</div>
                    <div>test</div>
                    <div>test</div>
                    <div>test</div>
                    <div>test</div>
                    <div>test</div>
                    <div>test</div>
                    <div>test</div>
                    <div>test</div>
                </LoadListBox>
            </BodyFrame>
        </>
    )
    
} 