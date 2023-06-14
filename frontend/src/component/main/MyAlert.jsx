import Swal from 'sweetalert2';

export default function MyAlert(type, message, after=() => {}){    
    Swal.fire({
        icon: type,
        text: message,
        confirmButtonText: '확인' 
    }).finally(() => {
        after();
    })
}