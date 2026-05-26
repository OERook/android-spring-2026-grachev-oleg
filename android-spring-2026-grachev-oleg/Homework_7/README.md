# Homework 7 — DI (Hilt) + Firebase

Расширение Homework 6: Rick & Morty search + details.

## Что реализовано

### Обязательная часть
1. **DI на Hilt.** `AppContainer` удалён, всё через модули в [di/](app/src/main/java/ru/itis/android/homework_6/di):
   - `NetworkModule` — OkHttp/Retrofit/Api
   - `DatabaseModule` — Room
   - `RepositoryModule` — `@Binds` для `CharacterRepository`
   - ViewModel'и помечены `@HiltViewModel`, в Compose — `hiltViewModel()`.

   **Динамическая передача аргумента через DI:**
   - `DetailsViewModel` получает `characterId` через `SavedStateHandle` (id выбирается на лету по клику в списке) — Hilt сам инжектит `SavedStateHandle` с nav-arg.
   - На стартовом экране используется динамический параметр, генерируемый на старте приложения: `UserIdProvider` создаёт UUID при первом запуске, кэширует его в SharedPreferences и инжектится в `SearchViewModel` (поле `currentUserId` отображается в шапке экрана) и в `MyApplication` (передаётся в Crashlytics).

2. **Crashlytics:**
   - `setUserId(uuid)` + `setCustomKey("user_id", uuid)` в `MyApplication.onCreate()`.
   - Логирование переходов: `FirebaseCrashlytics.log("screen_view: <route>")` в [AppNavigation.kt](app/src/main/java/ru/itis/android/homework_6/presentation/navigation/AppNavigation.kt) (плюс Analytics SCREEN_VIEW).

3. **Push-уведомления:** [AppFirebaseMessagingService](app/src/main/java/ru/itis/android/homework_6/push/AppFirebaseMessagingService.kt) обрабатывает **data**-пуши и ветвится по `data["kind"]`:
   - `promo` — высокий приоритет, иконка 🎁
   - `auth` — максимальный приоритет, иконка 🔐
   - другое — обычное уведомление

   `title` и `message` тянутся из data-payload.

### Опциональная часть
- Онбординг-экран при первом запуске ([OnboardingScreen.kt](app/src/main/java/ru/itis/android/homework_6/presentation/onboarding/OnboardingScreen.kt)). Закрывается только кнопкой, факт показа сохраняется в DataStore.
- Analytics-события: `onboarding_shown` (при отображении) и `onboarding_closed` (при нажатии кнопки).

## Перед сборкой

Нужно добавить `google-services.json` от своего Firebase-проекта в `app/`:

1. Создайте проект на https://console.firebase.google.com
2. Добавьте Android-приложение с `applicationId = ru.itis.android.homework_6`
3. Включите **Analytics**, **Crashlytics**, **Cloud Messaging**
4. Скачайте `google-services.json` и положите в `Homework_7/app/google-services.json`

После этого: `./gradlew assembleDebug`.

## Тестовый data-push

```json
{
  "to": "<FCM_TOKEN>",
  "data": {
    "kind": "promo",
    "title": "Скидка 50%",
    "message": "Только сегодня!"
  }
}
```

Token виден в logcat (`AppFCM: FCM token: ...`).
