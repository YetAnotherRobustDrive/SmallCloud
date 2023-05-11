import { TbEdit } from 'react-icons/tb'
import '../../css/mypage.css'

export default function EditableColumn(props) {

    return (
        <div className='editable-column'>
            <div className="texts">
                <span className="title">{props.title}</span>
                <span className="value">{props.value}</span>
            </div>
            <div className="icon" onClick={props.onSubmit}><TbEdit /></div>
        </div>
    )
}