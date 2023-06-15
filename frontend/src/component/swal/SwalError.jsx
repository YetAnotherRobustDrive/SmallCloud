import Swal from 'sweetalert2';

export default function SwalError(error) {
    const errorConverter = [

    ]
    
    Swal.fire({
        icon: 'error',
        text: error,
        confirmButtonText: '확인',
        allowOutsideClick: false,
    })
}