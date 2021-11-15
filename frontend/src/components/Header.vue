<template>
  <div class="bg-headerGray h-16 w-full grid grid-cols-2">
    <div class="h-16 ml-2 flex">
      <div
        class="grid justify-items-center items-center h-full w-auto ml-3 cursor-pointer mr-10"
        @click="goMain()"
      >
        <img src="@/images/logo.png" class="h-12 w-auto" />
      </div>
      <div
        v-if="projectName"
        class="sm:flex justify-center items-center text-white text-2xl hidden"
      >
        {{ projectName }}
      </div>
    </div>
    <div class="grid justify-items-end items-center h-16 mr-5">
      <div class="grid justify-items-center grid-cols-2 h-full">
        <div
          class="
            grid
            justify-items-center
            items-center
            relative
            h-full
            w-16
            cursor-pointer
            mr-2
            hover:opacity-50
          "
          @click="goAlarm()"
        >
          <i class="fas fa-bell text-white text-lg" />
          <div
            class="
              grid
              justify-items-center
              items-center
              absolute
              right-3
              bottom-4
              h-4
              w-4
              rounded-full
              bg-red-600
              text-white text-xs
              font-bold
            "
            v-if="this.cnt != ''"
          >
            {{ this.cnt }}
          </div>
        </div>

        <div
          class="h-full w-16 grid justify-items-center items-center cursor-pointer hover:opacity-50"
          @click="goProfile()"
        >
          <div class="rounded-full w-10 h-10 mr-3 flex">
            <img
              class="rounded-full flex object-cover w-full h-full"
              :src="'http://localhost:8080/img/' + id"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';

export default {
  name: 'Header',
  data() {
    return {
      showAlert: false,
      alarmList: [],
      cnt: '',
    };
  },
  components: {},
  created() {

    if (this.totalAlarmCnt > 9) this.cnt = '9+';
    else if (this.totalAlarmCnt == 0) this.cnt = '';
    else this.cnt = this.totalAlarmCnt;
  },
  methods: {
    goMain() {
      this.$router.push('/projects');
    },
    goAlarm() {
      this.$router.push({
        name: 'Alarm',
      });
    },
    goProfile() {
      this.$router.push(`/profile/${this.id}`);
    },
  },
  computed: {
    ...mapGetters(['projectName', 'id', 'totalAlarmCnt']),
  },
  watch: {
    totalAlarmCnt() {
      if (this.totalAlarmCnt > 9) this.cnt = '9+';
      else if (this.totalAlarmCnt == 0) this.cnt = '';
      else this.cnt = this.totalAlarmCnt;
    },
  },
};
</script>
