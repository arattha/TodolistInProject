import { axiosService } from '@/api/index.js';

function removeAlarm(alarmId, callback, errorCallback) {
    
    axiosService
        .put('/delAlarm', { params: { alarmId: alarmId } })
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
