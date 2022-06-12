# Changelog

## [0.3.1] - VFX & Balancing Update - 2022-06-12

### Mechanics

- Fix a bug where dead timelines can be triggered

### Cards

- Make Noxious Wraith exhaust and increase dexterity loss from 4(2) to 5(3)
- Fix non-upgraded description of Defective Echo

### VFX

- Add flying orb VFX when constructing a timeline
- Highlight the newly constructed timeline with a faint circle
- Change timeline aura color when it is being triggered and will play cards

## [0.3.0] - QoL & Content Update - 2022-06-10

### Mechanics

- Use `cardRandomRng` instead of `cardRng` when choosing a random timeline to not affect discovery
- Check `MAX_HAND_SIZE` instead of hard-coding 10 to support increased hand capacity from other mods
- Fix timelines being interrupted by an unplayable card
- Fix timeline interactions with Timer Eater: timelines are now interrupted by Time Warp power

### Character

- Change starting relic from Adaptive Shield to Antique Clock
- Buff starting HP from 70 to 77

### Cards

- Fix pluralization in cards
- Change Telekinesis to still trigger Thorns effect
- Change Adaptive Shield to an uncommon relic
- Rename Into The Void to Double Time
- Rework Double Time: always triggers a timeline once only, but costs 0 when upgraded
- Rework Overcore: always gives 1 Frozen, but is Exhaust if not upgraded
- Swap names of Misdirection and Avert
- Nerf Misdirection block gain from 10(15) to 8(12)
- Rework Transmute: always discard cards, discard 2 cards when upgraded
- Nerf Rewrite History damage from 2(3) to 1(2)
- Reduce Time Loop cost from 3 to 2(1)
- Rework Shortcut: reduce timeline HP cost by 5%(7%) instead of 8%, loses Innate
- Rework Evanescence: Whenever you draw a Status card, gain 3(5) block
- Nerf Defective Echo card count from 2(3) to 1(2)
- Fix a bug in Defective Echo that prevents you from using it in 2 consecutive rounds
- Buff Noxious Wraith Intangible gain from 1 to 2
- Change target of Divine Eye, Energy Conversion, Fast Forward, Hat Trick, Recollection, and Transmute from self to none
- Add new Curse card Singularity
- Add new Power card Quick Thinker
- Rework Shadow Form: Reduce timeline HP cost by 10%. All timeline cards construct +1 timeline. Ethereal. (Lose Ethereal
  when upgraded)

### Relics

- Rename Antique Stopwatch to Antique Clock
- Add new event relic Bottled Singularity
- Add new boss relic Atomic Clock

### Events

- Nerf Council of Ghosts event for the Retrospect: the Retrospect now only gets 3(2) Apparition cards
- Add Event Horizon event to give Bottled Singularity relic
- Change wordings in Mysterious Prophet event
- Add Future Gadgets event for non-Retrospect characters

### Potions

- Make Timeline Potion available for all characters
- Update Timeline Potion to be affected by Sacred Bark
- Add new uncommon potion Butterfly In A Jar

### UI

- Add a screen to view cards played this turn
- Add a screen to view cards in a timeline
- Add a peek button on timelines to bring up the card screen
- Fix a z-ordering issue of timeline cards in hand card select screen

### VFX

- Instead of disappearing instantly, play death animation when collapsing a timeline
- Fix Defect orbs not following the player when there are timelines
- Use a consistent card copying method in Timer power
- Remove card glow for timeline targeting cards when there are no timelines
- Add a special dialog for Time Eater when playing as the Retrospect

### Compatibility

- Fix a crash when dying as the Retrospect in Together In Spire

### Misc

- Migrate from Kobting/STSFriendlyMinions to hlysine/FriendlyMonsters for timelines

## [0.2.1] - Hotfix - 2022-05-23

### Mechanics

- Skip collapse timeline action if the timeline is already dead

### Cards

- Fixed a bug that prevents Into The Void+ from triggering a timeline twice
- Changed Temporal Anomaly to trigger timelines one after the other

### VFX
- Skip Rewrite History VFX if there are no cards in discard pile

## [0.2.0] - Second Technical Test - 2022-05-22

### Mechanics
- Timelines now play cards one after the other instead of queueing all cards at once
- Reworded timeline descriptions and keywords
- Timelines can now hold and play timeline construction cards
- Added new card attribute Delusional
- Added new card attribute Paradoxical
- Add new card targeting: timeline targeting
- Allow base game characters to also have max 5 concurrent timelines
- Added Time Loop mechanic with proper VFX

### Cards
- Changed Misdirection to play the attack card before constructing a timeline
- Changed Tap The Future to shuffle 1 instead of 2 Empty cards
- Fixed Rewrite History not exhausting
- Reworked Shadow Form to increase card draw by a constant amount instead of scaling up per round
- Changed Empty to be upgradable
- Added new status card Paradox
- Changed Sleight Of Hand to be able to replay other Sleight Of Hand cards
- Added Paradoxical to Sleight Of Hand
- Reworked Into The Void to target one timeline and no longer exhaust
- Added new cards: 
  - Time Loop
  - Shortcut
  - Electric Shock
  - Avert
  - Tactical Retreat
  - Lightspeed Leap
  - Fast Forward
  - All-Out Attack
  - Warm Up
  - Telekinesis
  - Recollection
  - Ambush
  - Hat Trick
  - Blind Strike
  - Evanescence
  - Soulless Demon
  - Noxious Wraith
  - Defective Echo
  - Divine Eye

### Events
- Added Mysterious Prophet event

### Powers
- Powers that play cards at the end of a turn now play them sequentially, right before timelines do
- Frozen power now allows all auto-played cards to be played
- Added new powers:
  - Defective Echo (card power)
  - Energized (card power, same as base game power but use custom energy icon)
  - Evanescence (card power)
  - Shortcut (card power)
  - Time Loop (card power)
  - Warm Up (card power)

### VFX
- Changed spire heart attack sequence slightly
- Added a VFX effect in Misdirection to show the chosen card before playing it
- Added death animation to timelines
- Changed timelines to use the same models, animations and corpse images as the selected character
- Fixed incorrect z-order of hovered timeline cards
- Timeline cards now appear at the timeline before hovering above the timeline instead of appearing at the discard pile
- Hide timeline cards when the player is dead
- Added a VFX effect in Sleight Of Hand to show the chosen card before playing it
- Corrected turn-based indicator for some powers
- Corrected buff/debuff VFX for some powers

### Misc
- Updated author field in code
- Updated base game to version 03-07-2022
- Updated dependencies

## [0.0.3] - Balancing Changes - 2022-04-08

### New content
- Added spire heart finisher sequence
- Added new relic: Slanted Mirror

### Changes
- Added additional safety checks in Alternative Reality
- Renamed Breakneck Form to Shadow Form and reworked it
- Reworked Rewrite History
- Timelines now cost 40% of your current HP
- Timeline construction cards can now be played even if the timeline cannot be constructed
- Updated the list of authors
- Changed the design of energy panel
- Changed energy orb icons for cards
- Highlight the actual action in Alternative Reality
- Remove `_V` suffix from Strike and Defend
- Cards are no longer marked as seen by default
- Changed Empty to be a colorless card
- Fixed timeline placement when the player is surrounded
- Increased starting HP from 65 to 70
- Decreased maximum number of timelines from 10 to 5
- Set Divert to be the default card for Match and Keep event
- Set ascension HP loss to 4
- Removed 1 Strike and 1 Defend from starting deck, making the starting deck size now 10
- Renamed Perpetual Hourglass to Quantum Hourglass and changed its flavor

## [0.0.2] - Hotfix - 2022-04-01

### Changed
- Updated strings
- Prevent the use of timeline potion if a timeline cannot be created

## [0.0.1] - First Technical Test - 2022-03-31

### New content
- First technical test
  - Timeline mechanics
  - 21 cards
  - 1 potion
  - 3 relics
