<div align="center">

# 🚏 <span style="font-size: 2.5em;">NextHalt</span>

<img width="100%" alt="NextHalt Preview" src="https://github.com/user-attachments/assets/3c5127eb-f181-4cd7-a2dc-67ff57c17d83" />

---

### **Know where your bus is. Tell the driver you are waiting at the halt.**

An Android app and a backend server that connect passengers and bus drivers in real time.

`Kotlin` · `Jetpack Compose` · `Ktor` · `PostgreSQL` · `Sri Lanka 🇱🇰`

🚧 **Status: Under development**

</div>

---

## 📑 Table of Contents

1. [About the Project](#1--about-the-project)
2. [How It Works](#2--how-it-works)
3. [Requirements and Priorities](#3--requirements-and-priorities)
4. [Feature Specs: Pages, UI Elements, API](#4--feature-specs)
5. [App Navigation Map](#5--app-navigation-map)
6. [Guidelines for the UI/UX Designer](#6--guidelines-for-the-uiux-designer)
7. [Architecture](#7--architecture)
8. [Folder Structure](#8--folder-structure)
9. [Backend and Database](#9--backend-and-database)
10. [Tech Stack](#10--tech-stack)
11. [How to Add a New Feature](#11--how-to-add-a-new-feature)
12. [Roadmap](#12--roadmap)
13. [Getting Started](#13--getting-started)
14. [Team and Git Workflow](#14--team-and-git-workflow)
15. [Glossary](#15--glossary)

---

## 1. 🎯 About the Project

<div align="center">
  <div style="display: flex; flex-direction: row; justify-content: center; align-items: center; gap: 32px; overflow-x: auto; padding: 12px 0;">
    <img src="https://github.com/user-attachments/assets/83b18f11-549d-40c9-b3f5-b179272f7410" alt="NextHalt UI Concept 1" width="260" style="border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.15);" />
    <img src="https://github.com/user-attachments/assets/ddebe7a8-b4aa-49ca-ba7e-bbd7ff1fdada" alt="NextHalt UI Concept 2" width="260" style="border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.15);" />
  </div>
</div>

### The problem
- A passenger does not know **where the bus is** or **when it will come**.
- A driver does not know **how many people are waiting** at the next halts.
- Many buses on the same route often run close together (bus bunching), while other halts have no bus for a long time.

### The solution
**Bus Tracker** is an app with two roles:

| Role | What they can do |
|---|---|
| 🧍 **Passenger** | Choose a route and time, then see the bus live on a map. See the current halt, the remaining distance and the ETA. Tap "I'm waiting" at a halt. Send parcels. |
| 🚍 **Driver** | Start a trip and share the phone's location. See other buses on the same route and how many passengers are waiting at the halts. Send a police alert. |

When a user registers, they choose a role. Each role gets a **different console**.

---

## 2. ⚙️ How It Works

```
   🚍 Driver phone                            🧍 Passenger phone
   (GPS location)                             (select route, I'm waiting)
         │                                           ▲        │
         ▼                                           │        ▼
   ┌─────────────────────────────────────────────────────────────────┐
   │                  Bus Tracker Server (Ktor)                      │
   │  Auth · Trips · Tracking · Waiting · Alerts · Parcels · ...     │
   └───────────────────────────────┬─────────────────────────────────┘
                                   ▼
                              PostgreSQL
```

### Core Ideas

1. **When the driver starts a trip**, the app reads the location from the phone's GPS. **No Google Maps API is needed** for this. Android's Fused Location Provider is free.
2. The server saves the **latest location** and pushes it through a **WebSocket** to the passengers who are watching that route.
3. **Halt detection:** When the driver's location is **within 100 meters** of the next halt (Haversine formula), the bus is treated as arrived. If auto detection fails, the driver can tap an **"Arrived" button**.
4. **ETA:** A halt-based calculation.
   ```
   ETA = (sum of average times between the halts from the current halt to the passenger's halt)
         - (time passed since the bus left the last halt)
   ```
   As more trips are recorded, these average times are improved using past data.
5. **Passenger location is not tracked all the time.** When the passenger taps "I'm waiting", the app reads the location **once** and checks if it is **within 150 meters** of the halt. The driver sees only the **count**, never the passenger's identity.
6. **Privacy:** The app asks for the user's consent every time it shares a location.

---

## 3. 📋 Requirements and Priorities

**Difficulty:** 🟢 Easy · 🟡 Medium · 🔴 Hard
**Priority:** `P1` = MVP · `P2` = Second · `P3` = Later

### 🏁 Features to Build First (Phase 1 / MVP)

**Passenger Side (P1)**
1. Register / Login (role: Passenger) 🟢
2. Navigate to the Passenger console 🟢
3. Select a route and time 🟢
4. Show the bus of the selected route live on the map 🟡
5. Show the halt where the bus is now 🟡
6. Show the remaining distance 🟡
7. ETA (arrival time) 🟡
8. Live updates (the screen updates by itself) 🟡

**Driver Side (P1)**
1. Register / Login (role: Driver) 🟢
2. Navigate to the Driver console 🟢
3. Trip start / end (select the route) 🟢
4. Stream the driver's live location (GPS) 🟡
5. Show other buses on the same route and where they are on the map 🟡

### 0. Common (For Both Roles)

| Requirement | Difficulty | Priority |
|---|:---:|:---:|
| Register / Login (choose a role: Passenger or Driver) | 🟢 | P1 |
| Show a different console for each role (role-based navigation) | 🟢 | P1 |
| Profile management | 🟢 | P2 |
| Language support (Sinhala / Tamil / English) | 🟡 | P3 |

### 1. Passenger Console

| Requirement | Difficulty | Priority |
|---|:---:|:---:|
| Select a route and time | 🟢 | P1 |
| Show the bus of the selected route live on the map | 🟡 | P1 |
| Show the halt where the bus is now | 🟡 | P1 |
| Show the remaining distance | 🟡 | P1 |
| ETA (arrival time) | 🟡 | P1 |
| Live updates (the screen updates by itself) | 🟡 | P1 |
| Push notification when the bus is near the halt | 🟡 | P2 |
| Driver / conductor phone numbers | 🟢 | P2 |
| Seat status inside the bus (empty seats) | 🟡 | P2 |
| Mark "I'm waiting" (at a halt) | 🟢 | P2 |
| Send a parcel (request, price, status tracking) | 🔴 | P3 |
| Ratings / complaints | 🟢 | P3 |
| SOS / trip share | 🟡 | P3 |

### 2. Driver Console

| Requirement | Difficulty | Priority |
|---|:---:|:---:|
| Trip start / end (select the route) | 🟢 | P1 |
| Stream the driver's live location (GPS) | 🟡 | P1 |
| Show other buses on the same route and where they are on the map | 🟡 | P1 |
| Show the number of waiting passengers at a halt | 🟡 | P2 |
| Police alert: report police on the road and notify other drivers on the same route in real time | 🟡 | P2 |
| Alert auto expires (after 30 minutes) and confirm/dismiss voting | 🟡 | P3 |
| Quick breakdown / accident report | 🟢 | P3 |
| Parcel manifest (list of parcels on the bus, confirm delivery) | 🔴 | P3 |

### Build Order

| Phase | Steps |
|---|---|
| **Phase 1 (MVP)** | 1. Auth + role selection<br>2. Driver: trip start + live location<br>3. Passenger: route select + live map + halt + distance + ETA<br>4. Driver: show buses on the same route |
| **Phase 2** | 5. Halt alerts (push notifications)<br>6. Waiting passengers count<br>7. Police alert<br>8. Phone numbers, seat status |
| **Phase 3** | 9. Parcel service, ratings, SOS, alert voting |

### ⚠️ Important Notes About Police Alerts
- It is dangerous for a driver to use the phone on the road. Add a **one-tap button** (a big icon).
- The alert must **attach the location automatically**.
- To stop fake alerts, give other drivers a **"Is it still there?"** confirm option.
- Check the **legal and policy side** too. Some countries limit speed trap alert apps.

---

## 4. 🧩 Feature Specs

> This part is for the **UI/UX designer** and the **developers**. For every feature it lists the **pages** needed, the **UI elements** on each page (buttons, text inputs, cards...), the **Android folder** and the **backend API**.

**UI element legend:** `Button` · `TextInput` · `Dropdown` · `Card` · `Chip` · `Switch` · `List` · `Map` · `BottomSheet` · `Dialog` · `Banner` · `Badge` · `Stepper`

**Page ID rule:** `C-xx` Common · `PA-xx` Passenger · `DR-xx` Driver. Use the same IDs as the names of the Figma frames.

---

### F01 · Auth and Role Selection `P1` `🟢`
Register, log in and choose a role (Passenger or Driver).

**`C-01` Splash**
- Logo, loading indicator
- If the token is valid, go to the Home of that role. If not, go to Welcome.

**`C-02` Welcome / Role Select**
- App logo and tagline
- `Card` x2: "I am a Passenger 🧍" and "I am a Driver 🚍" (one gets selected)
- `Button` (Primary): Continue
- `TextButton`: "Already have an account? Login"

**`C-03` Register**
- Top: a `Badge` that shows the selected role
- `TextInput`: Full name · Phone number · Email (optional) · Password · Confirm password
- **Driver only:** `TextInput` Bus number plate
- `Checkbox`: "I agree to the use of my location" + Terms link
- `Button` (Primary): Register
- Validation errors appear as red text under the input

**`C-04` Login**
- `TextInput`: Phone or Email · Password (with a show/hide icon)
- `Button` (Primary): Login
- `TextButton`: "No account? Register"

**States:** Button loading spinner · Error `Snackbar` (wrong password, no network)
**Android:** `features/auth/` · **API:** `POST /auth/register` · `POST /auth/login`

---

### F02 · Role-based Navigation `P1` `🟢`
After login, each role sees a different console.

- **Passenger Bottom Navigation:** 🏠 Home · ⏳ Waiting `P2` · 📦 Parcels `P3` · 👤 Profile
- **Driver Bottom Navigation:** 🚍 Trip · 👥 Waiting `P2` · 🚨 Alerts `P2` · 👤 Profile
- No new pages. Components: `BottomNavigationBar` (3-4 items), `TopAppBar`

**Android:** `navigation/` (RoleNavHost) · **API:** none (the role is inside the token)

---

### F03 · Select Route and Time `P1` `🟢` `Passenger`

**`PA-01` Passenger Home**
- Greeting text: "Hello, {name}"
- `Card` "Find a bus":
  - `Dropdown` (searchable bottom sheet): Route
  - `Dropdown`: My halt (enabled after a route is selected)
  - Time: `Chip` "Now" + `TimePicker`
  - `Button` (Primary): Find buses
- `List` of `Card`: Favorite routes `P2`
- Recent searches `P2`

**`PA-02` Available Buses**
- Top: Route name + selected halt + time
- `List` of `Card`. Each card has:
  - Bus number plate
  - Status `Chip`: Running 🟢 / Not started ⚪ / Delayed 🟠
  - "Now at: Kadawatha"
  - ETA `Badge`: "8 min"
  - Seat status `Chip` `P2`
- Tapping a card opens `PA-03`
- **Empty state:** "No bus at this time" + illustration

**Android:** `features/routesearch/`
**API:** `GET /routes` · `GET /routes/{id}/halts` · `GET /trips/active?routeId=`

---

### F04 · Live Bus Tracking `P1` `🟡` `Passenger`
Shows the bus on the map, the current halt, the remaining distance, the ETA and live updates.

**`PA-03` Live Tracking**
- `Map` (full screen): route line, halt markers, bus marker 🚍, "My halt" marker (a different color)
- Top: Back button + Route name
- `BottomSheet` card:
  - "Now at: **Kadawatha**" and "Next halt: Kiribathgoda"
  - Stats row x3: **Distance left** (km) · **ETA** (min) · **Halts left**
  - `Stepper`: halts shown as dots, with the bus position highlighted
  - "Last updated 12 seconds ago" (small text)
  - `Button`: 📞 Call driver `P2` · `Button`: I'm waiting `P2`
  - Seat status `Chip` `P2`
- `Banner`: "Reconnecting..." (when the internet is lost)

**Android:** `features/livetracking/`
**API:** `GET /trips/{id}` · `WS /ws/routes/{routeId}`

---

### F05 · Driver Trip: Start, End and Location `P1` `🟡` `Driver`

**`DR-01` Driver Home**
- Greeting
- `Card` "Today's route": Route name + Bus plate
- `Button` (Primary, big): ▶ Start Trip
- Past trips `List` `P3`

**`DR-02` Start Trip**
- `Dropdown`: Route · `Dropdown`: Bus (the driver's own bus by default)
- `Card` (permission explainer): "Your location is shared during the trip" + `Button` Allow location
- `Button` (Primary): Start Trip

**`DR-03` Active Trip**
- `Map`: route line, halts, my bus 🚍, other buses (F06)
- Top status bar: "Trip running · GPS ● good"
- `Card`: "Next halt: **Kadawatha** · 1.2 km"
- `Button` (big): ✅ Arrived at halt *(fallback if auto detection fails)*
- Seat status `SegmentedControl`: Empty / Half / Full `P2`
- `FAB` 🚨 Police alert `P2`
- `Tabs`/`BottomSheet`: Buses on route · Waiting `P2`
- `Button` (Secondary): End Trip → confirm `Dialog`

**`DR-07` Trip Summary**
- `Card`: duration, number of halts, distance
- `Button`: Done

**Android:** `features/trip/` (the Foreground Service for location lives here)
**API:** `POST /trips/start` · `POST /trips/{id}/location` · `POST /trips/{id}/halt-arrived` · `POST /trips/{id}/end`

---

### F06 · Other Buses on the Same Route `P1` `🟡` `Driver`

**"Buses on route" tab inside `DR-03`**
- Markers of other buses on the `Map`
- `List` of `Card`: Bus plate · "Ahead / Behind" · distance (km) · gap (min) · current halt
- Empty: "No other bus on this route"

**Android:** `features/nearbybuses/` · **API:** `GET /routes/{id}/active-trips`

---

### F07 · Profile Management `P2` `🟢`

**`C-05` Profile**
- Avatar (first letters of the name)
- `TextInput`: Name · Phone · Email
- `Button`: Save · `Button` (Danger text): Logout

**Android:** `features/profile/` · **API:** `GET /me` · `PUT /me`

---

### F08 · Halt Alerts (Push Notifications) `P2` `🟡` `Passenger`

**`PA-07` Alert Settings** (can also be a bottom sheet)
- `Switch`: Alerts on/off
- `Dropdown`: Alert halt
- `Chip`: 5 minutes before / 10 minutes before
- Example notification: "🚍 Your bus will reach Kiribathgoda halt in 5 minutes"

**Android:** `features/notifications/` · **API:** `POST /devices` (FCM token) · the server sends through FCM

---

### F09 · Driver / Conductor Phone Numbers `P2` `🟢` `Passenger`

**`BottomSheet` inside `PA-03`**
- Driver: name + phone + 📞 `Button` (opens the dialer)
- Conductor: name + phone + 📞 `Button`

**Android:** `features/contacts/` · **API:** the bus contact fields in the trip response

---

### F10 · Bus Seat Status `P2` `🟡`
- **Driver:** Empty / Half / Full `SegmentedControl` in `DR-03`
- **Passenger:** `Chip` in `PA-02` and `PA-03` (🟢 Empty · 🟠 Half · 🔴 Full)

**Android:** `features/seatstatus/` · **API:** `PUT /trips/{id}/seat-status`

---

### F11 · "I'm Waiting" and Waiting Board `P2` `🟢/🟡`

**`PA-05` Choose Halt** `Passenger`
- `Card`: "Your nearest halt: **Kadawatha** (80 m)"
- `List`: nearby halts sorted by distance
- `Button` (Primary): ✋ I'm waiting here
- Error `Dialog`: "You are not near the halt"

**`PA-06` Waiting Status** `Passenger`
- `Card`: Halt, Route, ⏳ "Expires in 28:10"
- ETA of the next bus
- `Button`: Cancel

**`DR-05` Waiting Board** `Driver`
- `List` of `Card` (next 3-5 halts): Halt name · **Count** `Badge` (big) · distance
- Empty: "Nobody is waiting"

**Android:** `features/waiting/` (passenger) · `features/waitingboard/` (driver)
**API:** `POST /waiting` · `DELETE /waiting/{id}` · `GET /halts/{id}/waiting-count` · `GET /trips/{id}/waiting-board`

---

### F12 · Police Alert `P2` / voting `P3` `🟡` `Driver`

**`DR-06` Police Alert**
- `FAB` (big, red/orange): 🚨 "Police ahead" · **one tap only**
- Confirm `BottomSheet`: location attached automatically, `Button` Send
- **Incoming alert:** `Banner`/`Dialog`: "🚨 Police reported · near Kiribathgoda · 4 min ago · 1.2 km ahead"
  - `Button`: Still there `P3` · `Button`: Not there now `P3`
- Auto expires after 30 minutes

**Android:** `features/policealert/` · **API:** `POST /alerts` · `POST /alerts/{id}/confirm` · `GET /alerts?routeId=` · `WS`

---

### F13 · Parcel Service `P3` `🔴`

**`PA-08` Send Parcel** `Passenger`
- `Dropdown`: Route · Pickup halt · Drop halt
- `Chip`: Size (S / M / L) · `TextInput`: Weight · Description
- `TextInput`: Receiver name · Receiver phone
- `Card`: Estimated price (automatic)
- `Button` (Primary): Request

**`PA-09` Parcel Tracking** `Passenger`
- `Stepper`: Requested → Accepted → On bus → Delivered
- `Card`: Pickup OTP / QR
- Bus info + driver call `Button`

**`DR-09` Parcel Manifest** `Driver`
- `List` of `Card`: Parcel ID · Pickup/Drop · Receiver
- `Button`: Pickup confirm (OTP `TextInput`) · `Button`: Delivered

**Android:** `features/parcel/` (passenger) · `features/parcelmanifest/` (driver)
**API:** `POST /parcels` · `GET /parcels/{id}` · `PUT /parcels/{id}/status`

---

### F14 · Ratings and Complaints `P3` `🟢`

**`PA-10` Rate & Report**
- Star rating (1-5)
- `Chip` (multi): Overcharging · Rash driving · Clean bus · Late
- `TextInput`: Comment
- `Button`: Submit

**Android:** `features/feedback/` · **API:** `POST /feedback`

---

### F15 · SOS and Trip Share `P3` `🟡`

**`PA-11` Safety**
- Big red `Button` 🆘 SOS (hold for 3 seconds)
- `Button`: Share trip (a link for the family)
- `List`: Emergency contacts + Add `Button`

**Android:** `features/safety/` · **API:** `POST /sos` · `POST /trips/{id}/share-link`

---

### F16 · Breakdown / Accident Report `P3` `🟢` `Driver`

**`DR-08` Quick Report**
- `Chip`: Breakdown · Accident · Traffic jam
- `TextInput`: Note (optional)
- Location attached automatically
- `Button`: Send
- Passengers see a `Banner`: "The bus is delayed"

**Android:** `features/incident/` · **API:** `POST /incidents`

---

### F17 · Language Support `P3` `🟡`

**`C-06` Settings**
- `RadioButton`: සිංහල · தமிழ் · English
- `Switch`: Notifications
- About

**Android:** `features/settings/` · All texts go into `strings.xml` locale files

---

## 5. 🗺️ App Navigation Map

```
Splash (C-01)
   │
   ├── no token ──► Welcome / Role Select (C-02) ──► Register (C-03) ──┐
   │                          └──────────────────────► Login (C-04) ────┤
   │                                                                    │
   └── has token ───────────────────────────────────────────────────────┤
                                                                        ▼
                      ┌─────────────────────────────┴──────────────────────────────┐
                      ▼                                                            ▼
              🧍 PASSENGER                                                    🚍 DRIVER
        ┌────────────────────┐                                         ┌──────────────────────┐
        │ Home (PA-01)       │                                         │ Home (DR-01)         │
        │   ▼                │                                         │   ▼                  │
        │ Buses (PA-02)      │                                         │ Start Trip (DR-02)   │
        │   ▼                │                                         │   ▼                  │
        │ Live Tracking      │──► Contacts (sheet)                     │ Active Trip (DR-03)  │──► Police Alert (DR-06)
        │ (PA-03)            │──► Alert Settings (PA-07)               │   ├ Buses on route   │──► Breakdown (DR-08)
        │   ▼                │                                         │   └ Waiting Board    │
        │ I'm waiting        │                                         │     (DR-05)          │
        │ (PA-05 → PA-06)    │                                         │   ▼                  │
        ├────────────────────┤                                         │ Trip Summary (DR-07) │
        │ Parcels (PA-08/09) │                                         ├──────────────────────┤
        │ Rate (PA-10)       │                                         │ Parcel Manifest      │
        │ SOS (PA-11)        │                                         │ (DR-09)              │
        └────────────────────┘                                         └──────────────────────┘
                      └──────────────► Profile (C-05) · Settings (C-06) ◄──────────────┘
```

---

## 6. 🎨 Guidelines for the UI/UX Designer

### Deliverables
- One Figma frame for every `Page ID`. Name it like this: `PA-03 Live Tracking`
- For every page, these states: **Default · Loading · Empty · Error · Offline**
- Reusable **components** (Button, Card, Chip, Bus Card, Halt Stepper, Seat Chip, Alert Banner)
- **Design tokens:** colors, typography, spacing, corner radius (the Android developers will put them in `core/designsystem`)

### Rules
- 🚍 **Driver screens:** The driver is driving, so use **big buttons (min 56dp)**, little text and icons that are easy to understand fast. Actions should take **one tap**.
- 🧍 **Passenger screens:** Give most of the space to the map. The bottom sheet shows only the most important information.
- Touch targets are at least **48dp**.
- **Sinhala and Tamil text** is longer than English (about 30-40%), so the layout must have room for it. Fonts: Noto Sans Sinhala / Noto Sans Tamil.
- Status colors: Running 🟢 · Delayed 🟠 · Not started ⚪ · Alert 🔴
- Both Light and Dark themes (Material 3)
- **Offline / Reconnecting** states are a must, because the network can be weak in some places.
- Icons: Material Symbols
- Alerts (police, breakdown) must be **easy to see**, but they must **not disturb** the driver.

---

## 7. 🏗️ Architecture

> **Goal:** When we build or change one feature, **no other feature should be affected.**

### Basic Rules

| # | Rule |
|---|---|
| 1 | **Feature-first:** We split the code into folders by **feature**, not by layer. |
| 2 | Every feature has 3 layers: `presentation` · `domain` · `data` |
| 3 | **Dependency Rule:** `presentation → domain ← data`. The `domain` layer is pure Kotlin (it imports nothing from Android, Ktor or the database). |
| 4 | **A feature never imports another feature.** Shared things go in `core/`. |
| 5 | Navigation between features is done by the `navigation/` package (route + arguments), so features do not know about each other. |
| 6 | If we delete a feature, the app must still compile. Only its one registration in navigation and DI should need to be removed. |
| 7 | **Duplicate** small models inside features instead of coupling features together. `core/model` holds only things that are truly global. |

### Android: Layers

```
┌─────────────────────────────────────────────────────────┐
│  presentation   Compose Screens · ViewModel · UiState   │
│        │                                                │
│        ▼                                                │
│  domain         UseCases · Models · Repository (interface)
│        ▲                                                │
│        │                                                │
│  data           Repository impl · API (Retrofit) · DTOs · Room
└─────────────────────────────────────────────────────────┘
```

- **Screen** (Compose): UI only. No logic.
- **ViewModel**: exposes a `StateFlow<UiState>` and calls UseCases.
- **UseCase**: one business action (for example, `StartTripUseCase`).
- **Repository (interface)**: lives in the domain layer. Its **implementation** lives in the data layer.
- **DTO → Domain model** mapping is done in the data layer.

### UI State Pattern

```
sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}
```

### Inside One Feature (Anatomy)

```
features/livetracking/
├── presentation/
│   ├── LiveTrackingScreen.kt
│   ├── LiveTrackingViewModel.kt
│   ├── LiveTrackingUiState.kt
│   └── components/          (UI parts used only by this feature)
├── domain/
│   ├── model/
│   ├── repository/TrackingRepository.kt
│   └── usecase/ObserveBusLocationUseCase.kt
├── data/
│   ├── remote/              (API, WebSocket, DTO)
│   ├── mapper/
│   └── repository/TrackingRepositoryImpl.kt
├── di/LiveTrackingModule.kt
└── LiveTrackingNavigation.kt   (route + NavGraphBuilder extension)
```

### How Do Features Talk to Each Other?

- **Navigation:** A feature takes a callback to go to the next screen (`onBusSelected(tripId)`). The callback is connected in `navigation/AppNavGraph.kt`.
- **Shared data:** Through `core/` (`SessionManager`, `LocationProvider`, `ApiClient`).
- **Backend events:** On the server, features use an in-process **event bus** (see section 9).

---

## 8. 📁 Folder Structure

### Repository (Root)

```
bus-tracker/
├── README.md
├── docs/
│   ├── architecture.md
│   ├── api.md                  (endpoint details)
│   ├── database.md             (ER diagram)
│   └── design/README.md        (Figma link, tokens, page IDs)
├── android-app/
├── backend/
└── .github/
    └── pull_request_template.md
```

### 📱 Android App

```
android-app/app/src/main/java/lk/bustracker/
├── BusTrackerApp.kt
├── MainActivity.kt
│
├── navigation/
│   ├── AppNavGraph.kt             (all features are connected here)
│   ├── RoleNavHost.kt             (Passenger / Driver bottom nav)
│   └── Destinations.kt
│
├── core/                          (shared by features, imports no feature)
│   ├── designsystem/              (Theme, Colors, Typography, reusable components)
│   ├── network/                   (ApiClient, AuthInterceptor, WebSocketClient)
│   ├── location/                  (LocationProvider, DistanceCalculator [Haversine])
│   ├── map/                       (MapView abstraction: can switch between MapLibre / Google Maps)
│   ├── storage/                   (TokenStore [DataStore], Room database)
│   ├── session/                   (SessionManager, UserRole)
│   ├── common/                    (UiState, Result, extensions)
│   └── di/                        (NetworkModule, StorageModule)
│
└── features/
    ├── auth/                      F01
    ├── routesearch/               F03   Passenger
    ├── livetracking/              F04   Passenger
    ├── contacts/                  F09   Passenger
    ├── notifications/             F08   Passenger
    ├── waiting/                   F11   Passenger
    ├── parcel/                    F13   Passenger
    ├── feedback/                  F14   Passenger
    ├── safety/                    F15   Passenger
    │
    ├── trip/                      F05   Driver  (Foreground Service location)
    ├── nearbybuses/               F06   Driver
    ├── seatstatus/                F10   Driver
    ├── waitingboard/              F11   Driver
    ├── policealert/               F12   Driver
    ├── incident/                  F16   Driver
    ├── parcelmanifest/            F13   Driver
    │
    ├── profile/                   F07   Common
    └── settings/                  F17   Common
```

> Every `features/<name>/` folder uses the same `presentation / domain / data / di` structure (see the Anatomy in section 7).

### 🖥️ Backend (Ktor)

```
backend/
├── build.gradle.kts
├── docker-compose.yml             (PostgreSQL)
└── src/main/
    ├── resources/
    │   ├── application.conf
    │   └── db/migration/          (Flyway: V1__core.sql, V2__trips.sql, ...)
    └── kotlin/lk/bustracker/
        ├── Application.kt
        │
        ├── plugins/               (Routing, Security [JWT], Serialization, Database, WebSockets)
        │
        ├── core/
        │   ├── config/
        │   ├── security/          (JwtService, PasswordHasher)
        │   ├── events/            (DomainEvents, EventBus)
        │   ├── errors/            (AppException, error responses)
        │   └── util/              (Haversine.kt, TimeUtils.kt)
        │
        ├── database/              (shared Exposed tables: Users, Routes, Halts, Buses)
        │
        └── features/
            ├── auth/              F01, F07
            ├── routes/            F03
            ├── trips/             F05, F06, F10
            ├── tracking/          F04   (WebSocket hub)
            ├── waiting/           F11
            ├── alerts/            F12, F16
            ├── notifications/     F08   (FCM)
            ├── parcels/           F13
            ├── feedback/          F14
            └── safety/            F15
```

Every backend feature folder looks like this:

```
features/trips/
├── TripRoutes.kt        (HTTP/WebSocket endpoints)
├── TripService.kt       (business logic)
├── TripRepository.kt    (database access)
├── TripDto.kt           (request/response models)
└── TripTables.kt        (DB tables of this feature)
```

---

## 9. 🗄️ Backend and Database

### Backend independence

- **Routes → Service → Repository** (one direction only)
- A feature never imports the `Service` or `Repository` of another feature.
- Features talk to each other through **`core/events`**:

| Event | Published by | Subscribed by |
|---|---|---|
| `TripLocationUpdated` | `trips` | `tracking` (WebSocket push) |
| `HaltArrived` | `trips` | `waiting` (marks the waiting records of that halt as served), `notifications` (halt alerts) |
| `AlertCreated` | `alerts` | `tracking` / `notifications` |

### Database Tables

```
users        (id, name, phone, email, password_hash, role, created_at)
routes       (id, name, start_halt, end_halt)
halts        (id, route_id, name, lat, lng, order_no, avg_minutes_to_next)
buses        (id, plate_number, route_id, driver_id, conductor_name, conductor_phone)
trips        (id, bus_id, route_id, driver_id, status, seat_status,
              current_halt_order, last_lat, last_lng, last_update, started_at, ended_at)
trip_events  (id, trip_id, halt_id, type, timestamp)
waiting      (id, route_id, halt_id, user_id, created_at, expires_at)
alerts       (id, route_id, type, lat, lng, created_by, created_at, expires_at)
alert_votes  (id, alert_id, user_id, still_there)
devices      (id, user_id, fcm_token)
parcels      (id, sender_id, route_id, pickup_halt, drop_halt, size, weight, receiver_name,
              receiver_phone, price, status, otp)
feedback     (id, user_id, trip_id, rating, tags, comment)
incidents    (id, trip_id, type, note, lat, lng, created_at)
sos_events   (id, user_id, lat, lng, created_at)
```

- **Shared tables** (`users`, `routes`, `halts`, `buses`) live in `database/`.
- Other tables live in the `*Tables.kt` file of their own feature, and they only use **foreign key IDs** to point to shared tables.
- Migrations are split by feature: `V3__waiting.sql`, `V4__alerts.sql` ...

### Live updates

```
WS  /ws/routes/{routeId}
```
The Passenger/Driver app connects to a route. The server pushes events: `location`, `halt_arrived`, `seat_status`, `alert`.

### Security
- Passwords are hashed with **BCrypt**.
- **JWT** access token (the role is a claim).
- Role guards: Driver endpoints allow only `DRIVER`, Passenger endpoints allow only `PASSENGER`.
- Location data: only while a trip is active. Passenger location is **not saved**.
- Rate limiting: `/waiting`, `/alerts`

### Validation (location related)
- "I'm waiting": check that the user is **within 150 meters** of the halt
- Driver location: check that it is near the route polyline and that the speed is reasonable
- Halt arrival: **within 100 meters** (Haversine)

---

## 10. 🧰 Tech Stack

### 📱 Android
| Part | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Navigation | Navigation Compose |
| DI | Hilt |
| Networking | Retrofit + OkHttp (REST + WebSocket) |
| Serialization | kotlinx.serialization |
| Local storage | DataStore (token), Room (offline queue) |
| Location | Fused Location Provider + Foreground Service |
| Map | MapLibre or Maps Compose (behind `core/map`, not decided yet) |
| Push | Firebase Cloud Messaging (P2) |
| Async | Coroutines + Flow |

### 🖥️ Backend
| Part | Technology |
|---|---|
| Language / Framework | Kotlin + Ktor |
| Database | PostgreSQL (PostGIS later) |
| ORM | Exposed + HikariCP |
| Migrations | Flyway |
| Auth | JWT + BCrypt |
| Live updates | Ktor WebSockets |
| Dev environment | Docker (docker-compose) |

---

## 11. ➕ How to Add a New Feature

When you add a new feature (for example, "Favorite routes"), follow this checklist. **You should not need to change the files of the old features.**

### Android
- [ ] Create the `features/<name>/` folder (`presentation / domain / data / di`)
- [ ] Screen + ViewModel + UiState
- [ ] UseCases + Repository interface (domain)
- [ ] API + DTO + Mapper + Repository impl (data)
- [ ] `di/<Name>Module.kt`
- [ ] `<Name>Navigation.kt` (route + NavGraphBuilder extension)
- [ ] Register it with **one line** in `navigation/AppNavGraph.kt`
- [ ] Check that it does **not** import any other feature

### Backend
- [ ] `features/<name>/` (`Routes / Service / Repository / Dto / Tables`)
- [ ] Flyway migration (`V<n>__<name>.sql`)
- [ ] Register the route in `plugins/Routing.kt`
- [ ] If other features need to know, use `core/events`

### Docs
- [ ] Add a spec for it to the Feature Specs (section 4) of this README
- [ ] Update `docs/api.md`
- [ ] Update the Figma page IDs

---

## 12. 🛣️ Roadmap

### Phase 1: MVP
- [ ] Project setup (Android + Ktor + PostgreSQL)
- [ ] F01 Auth + role selection
- [ ] F02 Role-based navigation
- [ ] F05 Driver trip start/end + live location
- [ ] F03 Passenger route + time selection
- [ ] F04 Live tracking + halt + distance + ETA
- [ ] F06 Buses on the same route (driver)

### Phase 2
- [ ] F11 I'm waiting + Waiting board
- [ ] F12 Police alert
- [ ] F08 Halt alerts (push)
- [ ] F09 Contacts
- [ ] F10 Seat status
- [ ] F07 Profile

### Phase 3
- [ ] F13 Parcel service
- [ ] F14 Ratings / complaints
- [ ] F15 SOS / trip share
- [ ] F16 Breakdown report
- [ ] F12 Alert voting
- [ ] F17 Language support (සිංහල / தமிழ் / English)
- [ ] Owner / Admin dashboard (in the future)

---

## 13. 🚀 Getting Started

> Update this section when the project is set up.

### Backend
**Needs:** JDK 17+, Docker

```
cd backend
docker compose up -d db
./gradlew run
```

**Environment variables**

| Variable | Description |
|---|---|
| `DB_URL` | Example: `jdbc:postgresql://localhost:5432/bustracker` |
| `DB_USER` / `DB_PASSWORD` | Database credentials |
| `JWT_SECRET` | The secret used to sign JWTs (do not commit it) |

### Android
**Needs:** Android Studio (latest stable), JDK 17

1. Open the `android-app/` folder in Android Studio.
2. Put the server URL in `local.properties` (for the emulator: `API_BASE_URL=http://10.0.2.2:8080/`).
3. Run on an emulator or a real device.

> To test the location feature, use **Extended controls → Location** in the emulator, or a real device.

---

## 14. 👥 Team and Git Workflow

### Team

| Role | Responsibility | Name |
|---|---|---|
| 🎨 UI/UX Designer | Pages, components, design tokens (Figma) | Dulana Chathurma |
| 🖥️ Backend Developer | Ktor server, database, API, project structure | W.D.Lemith  Nanditha |
| 📱 Android Developer | Compose screens, features |W.D.Lemith  Nanditha &   Dulana Chathurma  |

### Git Workflow
- `main`: stable, release
- `dev`: integration
- `feature/<feature-name>`: one branch for each feature (for example, `feature/live-tracking`)
- **One PR = one feature**
- Commit messages: `feat(tracking): add halt stepper` · `fix(auth): validate phone number` · `docs: update api.md`

### Designer → Developer Handoff
1. The Figma frame name is the Page ID (`PA-03 Live Tracking`).
2. All states (Default / Loading / Empty / Error / Offline) are in the frames.
3. Design tokens go in `docs/design/README.md`.
4. When a developer finishes a page, they add a screenshot to the PR.

---

## 15. 📖 Glossary

| Word | Meaning |
|---|---|
| **Halt** | A place where the bus stops (bus stop) |
| **Route** | The path the bus follows (an ordered list of halts) |
| **Trip** | One run of a bus on a route by a driver (from Start to End) |
| **ETA** | Estimated Time of Arrival |
| **Polyline** | The line that draws the route on the map |
| **Haversine** | A formula that calculates the distance between two GPS points |
| **Foreground Service** | An Android service that can read the location even when the app is in the background (it shows a notification) |
| **WebSocket** | A connection that lets the server push live data to the app |
| **FCM** | Firebase Cloud Messaging, used to send push notifications |
| **Bus bunching** | Many buses of the same route running close together |

---

<div align="center">

**Made with ❤️ in Sri Lanka 🇱🇰**

</div>
