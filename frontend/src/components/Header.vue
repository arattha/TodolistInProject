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
          @click="goAlert()"
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
          >
            9+
          </div>
        </div>

        <div
          class="h-full w-16 grid justify-items-center items-center cursor-pointer hover:opacity-50"
          @click="goProfile()"
        >
          <i class="fas fa-user-circle text-white text-3xl"></i>
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
    };
  },
  components: {},
  methods: {
    goMain() {
      this.$router.push('/');
    },
    goAlert() {
      this.$router.push('/alert');
    },
    goProfile() {
      this.$router.push('/profile');
    },
  },
  computed: {
    ...mapGetters(['projectName']),
  },
};
</script>
