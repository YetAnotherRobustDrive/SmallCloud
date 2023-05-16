import { TbEdit } from 'react-icons/tb'
import '../../css/mypage.css'

export default function EditableColumn(props) {

    return (
        <div className='editable-column'>
            <div className="texts">
                <span className="title">{props.title}</span>
                <input 
                name={props.name}
                className="value" 
                defaultValue={props.value}
                type="text"
                size="10" 
                disabled={props.disabled}/>
            </div>
            {(props.title == "PW") &&
                <button onClick={props.onClick} className="icon" ><TbEdit /></button>
            }
        </div>
    )
}