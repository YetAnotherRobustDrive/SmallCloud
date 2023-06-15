import Swal from 'sweetalert2';

export default function SwalAlert(type, message, after=() => {}){    
    Swal.fire({
        icon: type,
        text: message,
        confirmButtonText: '확인',
        allowOutsideClick: false,
        willClose: () => after(),
    })
}