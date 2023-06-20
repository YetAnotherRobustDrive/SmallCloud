import Swal from 'sweetalert2';

export default function SwalLoadingBy(message){    
    Swal.fire({
        title: message,
        showConfirmButton: false,
        allowOutsideClick: false,
        willOpen: () => {
            Swal.showLoading();
        },
        willClose: () => {
            Swal.hideLoading();
        }, 
    })
}