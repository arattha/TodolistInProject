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
          class="h-full w-16 relative grid justify-items-center items-center cursor-pointer"
          @click="toggleMenu()"
        >
          <div class="rounded-full w-10 h-10 mr-3 flex hover:opacity-50">
            <img
              class="rounded-full flex object-cover w-full h-full"
              :src="'http://localhost:8080/img/' + id"
            />
          </div>
          <div
            class="
              flex flex-col
              absolute
              top-12
              right-4
              h-20
              w-24
              bg-contentGray
              rounded-md
              shadow-md
              border
            "
            v-if="isShowMenu"
            v-click-outside="onClickOutside"
          >
            <div
              class="
                flex
                justify-center
                items-center
                h-1/2
                hover:bg-itemGray
                cursor-pointer
                text-sm
                font-bold
              "
              @click="goProfile"
            >
              내 정보
            </div>
            <div
              class="
                flex
                justify-center
                items-center
                h-1/2
                hover:bg-itemGray
                cursor-pointer
                text-sm
                font-bold
              "
              @click="logout"
            >
              로그아웃
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import vClickOutside from 'v-click-outside';
import { logoutUser } from '@/api/auth.js';
import { mapGetters, mapActions } from 'vuex';

export default {
  name: 'Header',
  directives: {
    clickOutside: vClickOutside.directive,
  },
  data() {
    return {
      showAlert: false,
      isShowMenu: false,
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
    ...mapActions(['toggle_isLogin', 'set_id', 'set_nickname']),
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
    onClickOutside() {
      this.isShowMenu = false;
    },
    toggleMenu() {
      this.isShowMenu = !this.isShowMenu;
    },
    logout() {
      logoutUser(
        this.id,
        () => {
          this.set_id('');
          this.set_nickname('');
          this.toggle_isLogin(false);

          this.$router.push('/login');
        },
        () => {
          this.set_id('');
          this.set_nickname('');
          this.toggle_isLogin(false);

          this.$router.push('/login');
        }
      );
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
