# FITFORGE Android starter

A native Kotlin + Jetpack Compose Android app starter with:
- Separate Gym and Home workout modes
- Indian food calorie/protein database (approximate per 100g)
- Daily calorie and protein totals
- BMI and protein estimate tools
- Water, sleep and manually updated NoFap streak trackers
- Workout completion history
- Neon Green / Blue / Violet accent themes
- SharedPreferences persistence for core counters
- GitHub Actions workflow to build a debug APK in the cloud

## Build APK using GitHub from an Android phone
1. Create a GitHub repository named `FITFORGE`.
2. Upload the contents of this project (not the enclosing folder) to the repository.
3. Open the **Actions** tab and allow workflows if prompted.
4. Run **Build FITFORGE APK** using **Run workflow**, or push a commit to `main`.
5. Open the completed workflow run and download the `FITFORGE-debug-apk` artifact.
6. Extract the artifact ZIP on your phone and install `app-debug.apk`. Android may ask you to allow installs from your browser/file manager.

## Important limitations in this starter
This is a first runnable foundation, not a finished commercial-grade product. The food database is illustrative and values vary by recipe and portion. Data is stored locally on the device. Cloud sync, photo gallery integration, scheduled notifications, exercise video library, advanced workout logging, full TDEE calculator, and a date-aware streak engine still need implementation. No account or cloud service is required for the current offline features.

For safe training, adapt loads and exercises to your ability. BMI is a screening measure, not a diagnosis.
