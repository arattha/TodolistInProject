import { axiosService } from '@/api/index.js';

function getTeam(projectId, callback, errorCallback) {
    
    axiosService
        .get('/team', { params: { projectId: projectId } })
        .then((res) => {
            callback(res.data);
        })
        .catch((err) => {
            errorCallback(err);
        });

}

export { getTeam };
