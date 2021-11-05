import { axiosService } from '@/api/index.js';

function removeAlarm(data, callback, errorCallback) {
    
    axiosService
        .put('/delAlarm', data)
        .then((res) => {
            callback(res.data);
        })
        .catch((err) => {
            errorCallback(err);
        });

}

function removeAllAlarm(memberId, callback, errorCallback) {
    
    axiosService
        .put('/delAll', { params: { memberId: memberId } })
        .then((res) => {
            callback(res.data);
        })
        .catch((err) => {
            errorCallback(err);
        });

}

export { removeAlarm, removeAllAlarm };
