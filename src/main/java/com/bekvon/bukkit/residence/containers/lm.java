package com.bekvon.bukkit.residence.containers;

import com.bekvon.bukkit.residence.Residence;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;

public enum lm {
    Invalid_Player("&cInvalid player name..."),
    Invalid_PlayerOffline("&cPlayer is offline"),
    Invalid_World("&cInvalid world..."),
    Invalid_Residence("&cInvalid Residence..."),
    Invalid_Subzone("&cInvalid Subzone..."),
    Invalid_Direction("&cInvalid Direction..."),
    Invalid_Amount("&cInvalid Amount..."),
    Invalid_Cost("&cInvalid Cost..."),
    Invalid_Days("&cInvalid number of days..."),
    Invalid_Material("&cInvalid Material..."),
    Invalid_Boolean("&cInvalid value, must be &6true(t) &cor &6false(f)"),
    Invalid_Area("&cInvalid Area..."),
    Invalid_Group("&cInvalid Group..."),
    Invalid_Location("&cInvalid Location..."),
    Invalid_MessageType("&cMessage type must be enter or remove."),
    Invalid_Flag("&cInvalid Flag..."),
    Invalid_FlagType_Fail("&cInvalid Flag... This flag can only be used on %1"),
    Invalid_FlagType_Player("Player"),
    Invalid_FlagType_Residence("Residence"),
    Invalid_FlagState("&cInvalid flag state, must be &6true(t)&c, &6false(f)&c, or &6remove(r)"),
    Invalid_List("&eUnknown list type, must be &6blacklist &eor &6ignorelist."),
    Invalid_Page("&eInvalid Page..."),
    Invalid_Help("&cInvalid Help Page..."),
    Invalid_NameCharacters("&cName contained unallowed characters..."),
    Invalid_PortalDestination("&cPortal destination is in restricted zone. Portal creation canceled. &7Find new location"),

    Invalid_FromConsole("&cYou can only use this in the console!"),
    Invalid_Ingame("&cYou can only use this in game!"),

    Area_Exists("&cArea name already exists."),
    Area_Create("&eResidence Area created, ID &6%1"),
    Area_DiffWorld("&cArea is in a different world from residence."),
    Area_Collision("&cArea collides with residence &6%1"),
    Area_TooClose("&cToo close to another residence. You need atleast &e%1 &cblock gap."),
    Area_SubzoneCollision("&cArea collides with subzone &6%1"),
    Area_NonExist("&cNo such area exists."),
    Area_InvalidName("&cInvalid Area Name..."),
//    Area_ToSmallTotal("&cSelected area smaller than allowed minimal (&6%1&c)"),
    Area_ToSmallX("&cYour &6X &cselection length (&6%1&c) is too small. &eAllowed &6%2 &eand more."),
    Area_ToSmallY("&cYour selection height (&6%1&c) is too small. &eAllowed &6%2 &eand more."),
    Area_ToSmallZ("&cYour &6Z &cselection length (&6%1&c) is too small. &eAllowed &6%2 &eand more."),
    Area_ToBigX("&cYour &6X &cselection length (&6%1&c) is too big. &eAllowed &6%2 &eand less."),
    Area_ToBigY("&cYour selection height (&6%1&c) is too big. &eAllowed &6%2 &eand less."),
    Area_ToBigZ("&cYour &6Z &cselection length (&6%1&c) is too big. &eAllowed &6%2 &eand less."),
    Area_Rename("&eRenamed area &6%1 &eto &6%2"),
    Area_Remove("&eRemoved area &6%1..."),
    Area_Name("&eName: &2%1"),

    Area_ListAll("&a{&eID:&c%1 &eP1:&c(%2,%3,%4) &eP2:&c(%5,%6,%7) &e(Size:&c%8&e)&a}"),

    Area_RemoveLast("&cCannot remove the last area in a residence."),
    Area_NotWithinParent("&cArea is not within parent area."),
    Area_Update("&eArea Updated..."),
    Area_MaxPhysical("&eYou've reached the max physical areas allowed for your residence."),
    Area_SizeLimit("&eArea size is not within your allowed limits."),
    Area_HighLimit("&cYou cannot protect this high up, your limit is &6%1"),
    Area_LowLimit("&cYou cannot protect this deep, your limit is &6%1"),

    Area_WeirdShape("&3Residence is out of regular shape. &6%1 &3side is &6%2 &3times bigger than &6%3 &3side"),

    Select_Points("&eSelect two points first before using this command!"),
    Select_Overlap("&cSelected points overlap with &6%1 &cregion!"),
    Select_KingdomsOverlap("&cSelected points overlap with &6%1 &cKingdoms land!"),
    Select_Success("&eSelection Successful!"),
    Select_Fail("&cInvalid select command..."),
    Select_Bedrock("&eSelection expanded to your lowest allowed limit."),
    Select_Sky("&eSelection expanded to your highest allowed limit."),
    Select_Area("&eSelected area &6%1 &eof residence &6%2"),
    Select_Tool("&e- Selection Tool: &6%1"),
    Select_PrimaryPoint("&ePlaced &6Primary &eSelection Point %1"),
    Select_SecondaryPoint("&ePlaced &6Secondary &eSelection Point %1"),
    Select_Primary("&ePrimary selection: &6%1"),
    Select_Secondary("&eSecondary selection: &6%1"),
    Select_TooHigh("&cWarning, selection went above top of map, limiting."),
    Select_TooLow("&cWarning, selection went below bottom of map, limiting."),
    Select_TotalSize("&eSelection total size: &6%1"),
    Select_AutoEnabled("&eAuto selection mode turned &6ON&e. To disable it write &6/res select auto"),
    Select_AutoDisabled("&eAuto selection mode turned &6OFF&e. To enable it again write &6/res select auto"),
    Select_Disabled("&cYou don't have access to selections commands"),

    Sign_Updated("&6%1 &esigns updated!"),
    Sign_TopLine("[res]"),
    Sign_DateFormat("YY/MM/dd HH:mm"),
    Sign_LookAt("&cYou are not looking at sign"),
    Sign_TooMany("&cToo many signs for this residence"),
    Sign_ResName("&0%1"),
    Sign_Owner("&0%1"),


    info_years("&e%1 &6years "),
    info_oneYear("&e%1 &6year "),
    info_day("&e%1 &6days "),
    info_oneDay("&e%1 &6day "),
    info_hour("&e%1 &6hours "),
    info_oneHour("&e%1 &6hour "),
    info_min("&e%1 &6min "),
    info_sec("&e%1 &6sec "),

    info_listSplitter(", "),

    info_click("&7Click"),
    info_clickToConfirm("&7Click to confirm"),

    server_land("Server_Land"),

    Flag_p1Color("&2"),
    Flag_p2Color("&a"),
    Flag_haveColor("&2"),
    Flag_havePrefix(""),
    Flag_lackColor("&7"),
    Flag_lackPrefix(""),
    Flag_others("&eand &2%1 &eothers"),
    Flag_Set("&eFlag (&6%1&e) set for &6%2 &eto &6%3 &estate"),
    Flag_Removed("&eFlag (&6%1&e) removed for &6%2"),
    Flag_PSet("&eFlag (&6%1&e) set for &6%2 &eto &6%3 &estate for &6%4"),
    Flag_PRemoved("&eFlag (&6%1&e) removed from &6%2 &eresidence for &6%3"),
    Flag_SetFailed("&cYou don't have access to &6%1 &cflag"),
    Flag_CheckTrue("&eFlag &6%1 &eapplies to player &6%2 &efor residence &6%3&e, value = &6%4"),
    Flag_CheckFalse("&eFlag &6%1 &edoes not apply to player &6%2 &efor residence."),
    Flag_Cleared("&eFlags Cleared."),
    Flag_RemovedAll("&eAll flags removed for &6%1 &ein &6%2 &eresidence."),
    Flag_RemovedGroup("&eAll flags removed for &6%1 &egroup in &6%2 &eresidence."),
    Flag_Default("&eFlags set to default."),
    Flag_Deny("&cYou don't have &6%1 &cpermission<s> here."),
    Flag_SetDeny("&cOwner does not have access to flag &6%1"),
    Flag_ChangeDeny("&cYou cant change &6%1 &cflag state while there is &6%2 &cplayer(s) inside."),
    Flag_ChangedForOne("&eChanged &6%1 &eflags for &6%2 &eresidences"),
    Flag_ChangedFor("&eChanged &6%1 &eflags from &6%2 &eresidences checked"),
    Flag_reset("&eReset flags for &6%1 &eresidence"),
    Flag_resetAll("&eReset flags for &6%1 &eresidences"),


    Subzone_Rename("&eRenamed subzone &6%1 &eto &6%2"),
    Subzone_Remove("&eSubzone &6%1 &eremoved."),
    Subzone_Create("&eCreated Subzone &6%1"),
    Subzone_CreateFail("&cUnable to create subzone &6%1"),
    Subzone_Exists("&cSubzone &6%1 &calready exists."),
    Subzone_Collide("&cSubzone collides with subzone &6%1"),
    Subzone_MaxAmount("&cYou have reached the maximum allowed subzone amount for this residence."),
    Subzone_MaxDepth("&cYou have reached the maximum allowed subzone depth."),
    Subzone_SelectInside("&eBoth selection points must be inside the residence."),
    Subzone_CantCreate("&cYou don't have permission to create residence subzone."),
    Subzone_CantDelete("&cYou don't have permission to delete residence subzone."),
    Subzone_CantDeleteNotOwnerOfParent("&cYou are not owner of parent residence to delete this subzone."),
    Subzone_CantContract("&cYou don't have permission to contract residence subzone."),
    Subzone_CantExpand("&cYou don't have permission to expand residence subzone."),
    Subzone_DeleteConfirm("&eAre you sure you want to delete subzone &6%1&e, use &6/res confirm &eto confirm."),
    Subzone_OwnerChange("&eSubzone &6%1 &eowner changed to &6%2"),

    Residence_DontOwn("&eNothing to show"),
    Residence_Hidden(" &e(&6Hidden&e)"),
    Residence_TooMany("&cYou already own the max number of residences your allowed to."),
    
    Residence_AlreadyExists("&cA residence named &6%1 &calready exists."),
    Residence_Create("&eYou have created residence &6%1&e!"),
    Residence_Rename("&eRenamed Residence &6%1 &eto &6%2"),
    Residence_Remove("&eResidence &6%1 &ehas been removed..."),
    Residence_CantRemove("&cResidence &6%1 &ccant be removed as &6%2 &csubzone is still rented by &6%3"),
    Residence_MoveDeny("&cYou don't have movement permission for Residence &6%1"),
    Residence_TeleportNoFlag("&cYou don't have teleport access for that residence."),
    Residence_FlagDeny("&cYou don't have &6%1 &cpermission for &6%2 &cresidence"),
    Residence_BaseFlagDeny("&cYou don't have &6%1 &cpermission"),
    Residence_GiveLimits("&cCannot give residence to target player, because it is outside the target players limits."),
    Residence_GiveConfirm("&7Click to confirm &6%1 &7residence transfer from &6%2 &7to &6%3"),
    Residence_Give("&eYou give residence &6%1 &eto player &6%2"),
    Residence_Received("&eYou have received residence &6%1 &efrom player &6%2"),
    Residence_ResList(" &a%1. &e%2 &e- &6%3 %4&6%5"),
    Residence_TrustedResList(" &a%1. &f%2 &e- &6%3 %4&6%5"),
    Residence_List(" &e%2 &e- &6%3"),
    Residence_Near("&eNearby residences: &7%1"),
    Residence_TeleportNear("&eTeleported to near residence."),
    Residence_SetTeleportLocation("&eTeleport Location Set..."),
    Residence_TeleportBlockedWorlds("&cCan't teleport to residence in blocked world"),
    Residence_PermissionsApply("&ePermissions applied to residence."),
    Residence_NotOwner("&cYou are not owner of this residence"),
    Residence_RemovePlayersResidences("&eRemoved all residences belonging to player &6%1"),
    Residence_NotIn("&cYou are not in a Residence."),
    Residence_PlayerNotIn("&cPlayer standing not in your Residence area."),
    Residence_Kicked("&eYou were kicked from residence"),
    Residence_CantKick("&eCan't kick this player"),
    Residence_In("&eYou are standing in Residence &6%1"),
    Residence_OwnerChange("&eResidence &6%1 &eowner changed to &6%2"),
    Residence_NonAdmin("&cYou are not a Residence admin."),
    Residence_Line("&eResidence: &6%1 "),
    Residence_MessageChange("&eMessage Set..."),
    Residence_CantDeleteResidence("&cYou don't have permission to delete residence."),
    Residence_CantExpandResidence("&cYou don't have permission to expand residence."),
    Residence_CantContractResidence("&cYou don't have permission to contract residence."),
    Residence_NoResHere("&cThere is no residence in there."),
    Residence_OwnerNoPermission("&cThe owner does not have permission for this."),
    Residence_ParentNoPermission("&cYou don't have permission to make changes to the parent zone."),
    Residence_DeleteConfirm("&eAre you sure you want to delete residence &6%1&e, use &6/res confirm &eto confirm."),
    Residence_ChangedMain("&eChanged main residence to &6%1"),

    Residence_Balance("&eBalance: &6%1"),

    command_addedAllow("&eAdded new allowed command for &6%1 &eresidence"),
    command_removedAllow("&eRemoved allowed command for &6%1 &eresidence"),
    command_addedBlock("&eAdded new blocked command for &6%1 &eresidence"),
    command_removedBlock("&eRemoved blocked command for &6%1 &eresidence"),
    command_Blocked("&eBlocked commands: &6%1"),
    command_Allowed("&eAllowed commands: &6%1"),

    command_Parsed("%1"),
    command_PlacehlderList("&e%1. &6%2"),
    command_PlacehlderResult(" &eresult: &6%1"),


    Economy_NotEnoughMoney("&cYou don't have enough money."),
    Economy_NotEnoughMoneyAmount("&cYou don't have enough money. (&6%1&c)"),
    Economy_MoneyCharged("&eCharged &6%1 &eto your &6%2 &eaccount."),
    Economy_MoneyAdded("&eGot &6%1 &eto your &6%2 &eaccount."),
    Economy_MoneyCredit("&eCredited &6%1 &eto your &6%2 &eaccount."),
    Economy_MarketDisabled("&cEconomy Disabled!"),

    Expanding_North("&eExpanding North &6%1 &eblocks"),
    Expanding_West("&eExpanding West &6%1 &eblocks"),
    Expanding_South("&eExpanding South &6%1 &eblocks"),
    Expanding_East("&eExpanding East &6%1 &eblocks"),
    Expanding_Up("&eExpanding Up &6%1 &eblocks"),
    Expanding_Down("&eExpanding Down &6%1 &eblocks"),

    Contracting_North("&eContracting North &6%1 &eblocks"),
    Contracting_West("&eContracting West &6%1 &eblocks"),
    Contracting_South("&eContracting South &6%1 &eblocks"),
    Contracting_East("&eContracting East &6%1 &eblocks"),
    Contracting_Up("&eContracting Up &6%1 &eblocks"),
    Contracting_Down("&eContracting Down &6%1 &eblocks"),

    Shifting_North("&eShifting North &6%1 &eblocks"),
    Shifting_West("&eShifting West &6%1 &eblocks"),
    Shifting_South("&eShifting South &6%1 &eblocks"),
    Shifting_East("&eShifting East &6%1 &eblocks"),
    Shifting_Up("&eShifting Up &6%1 &eblocks"),
    Shifting_Down("&eShifting Down &6%1 &eblocks"),

    Limits_PGroup("&7- &ePermissions Group:&3 %1"),
    Limits_RGroup("&7- &eResidence Group:&3 %1"),
    Limits_Admin("&7- &eResidence Admin:&3 %1"),
    Limits_CanCreate("&7- &eCan Create Residences:&3 %1"),
    Limits_MaxRes("&7- &eMax Residences:&3 %1"),
    Limits_MaxEW("&7- &eMax East/West Size:&3 %1"),
    Limits_MaxNS("&7- &eMax North/South Size:&3 %1"),
    Limits_MaxUD("&7- &eMax Up/Down Size:&3 %1"),
    Limits_MinMax("&7- &eMin/Max Protection Height:&3 %1 to %2"),
    Limits_MaxSubzones("&7- &eMax Subzones:&3 %1"),
    Limits_MaxSubDepth("&7- &eMax Subzone Depth:&3 %1"),
    Limits_EnterLeavePrefix(""),
    Limits_EnterLeave("&7- &eCan Set Enter/Leave Messages:&3 %1"),
    Limits_NumberOwn("&7- &eNumber of Residences you own:&3 %1"),
    Limits_Cost("&7- &eResidence Cost Per Block:&3 %1"),
    Limits_Flag("&7- &eFlag Permissions:&3 %1"),

    Gui_Set_Title("&8%1 flags"),
    Gui_Pset_Title("&8%1 &7%2 &6flags"),
    Gui_Flag_NameColor("&7"),
    Gui_Actions(new ArrayList<String>(Arrays.asList("&2Left click to enable", "&cRight click to disable", "&eShift + left click to remove"))),

    InformationPage_Top("&e___/ &a %1 - %2 &e \\___"),
    InformationPage_TopSingle("&e___/ &a %1 &e \\___"),
    InformationPage_Page("&e-----< &6%1 &e>-----"),
    InformationPage_NextPage2("&e-----< &6%1 &e>-----"),
    InformationPage_NoNextPage("&e-----------------------"),
    InformationPage_GeneralList("&2 %1 &6- &e%2"),
    InformationPage_FlagsList("&2 %1 &6- &e%2"),
    InformationPage_SmallSeparator("&6------"),



    RandomTeleport_TpLimit("&eYou can't teleport so fast, please wait &6%1 &esec and try again"),
    RandomTeleport_TeleportSuccess("&eTeleported to X:&6%1&e, Y:&6%2&e, Z:&6%3 &elocation"),
    RandomTeleport_IncorrectLocation("&6Could not find correct teleport location, please wait &e%1 &6sec and try again."),
    RandomTeleport_Disabled("&cRandom teleportation is disabled in this world"),
    RandomTeleport_TeleportStarted("&eTeleportation started, don't move for next &6%4 &esec."),
    RandomTeleport_WorldList("&ePossible worlds: &6%1"),

    Permissions_variableColor("&f"),
    Permissions_permissionColor("&6"),
    Permissions_cmdPermissionColor("&2"),

    General_DisabledWorld("&cResidence plugin is disabled in this world"),
    General_CantCreate("&cCan't create residences in this world"),
    General_UseNumbers("&cPlease use numbers..."),
    General_CantPlaceLava("&cYou can't place lava outside residence and higher than &6%1 &cblock level", "Replace all text with '' to disable this message"),
    General_CantPlaceWater("&cYou can't place Water outside residence and higher than &6%1 &cblock level", "Replace all text with '' to disable this message"),
    General_CantPlaceChest("&cYou can't place chest at this place"),
    General_NoPermission("&cYou don't have permission for this."),
    General_info_NoPlayerPermission("&c[playerName] doesn't have [permission] permission"),
    General_NoCmdPermission("&cYou don't have permission for this command."),
    General_DefaultUsage("&eType &6/%1 ? &efor more info"),
    General_MaterialGet("&eThe material name for ID &6%1 &eis &6%2"),
    General_MarketList("&e---- &6Market List &e----"),
    General_Separator("&e----------------------------------------------------"),
    General_AdminOnly("&cOnly admins have access to this command."),
    General_InfoTool("&e- Info Tool: &6%1"),
    General_ListMaterialAdd("&6%1 &eadded to the residence &6%2"),
    General_ListMaterialRemove("&6%1 &eremoved from the residence &6%2"),
    General_ItemBlacklisted("&cYou are blacklisted from using this item here."),
    General_WorldPVPDisabled("&cWorld PVP is disabled."),
    General_NoPVPZone("&cNo PVP zone."),
    General_NoFriendlyFire("&cNo friendly fire"),
    General_InvalidHelp("&cInvalid help page."),

    General_TeleportDeny("&cYou don't have teleport access."),
    General_TeleportSuccess("&eTeleported!"),
    General_TeleportConfirmLava("&cThis teleport is not safe, you will fall into &6lava&c. Use &6/res tpconfirm &cto perform teleportation anyways."),
    General_TeleportConfirmVoid("&cThis teleport is not safe, you will fall into &6void&c. Use &6/res tpconfirm &cto perform teleportation anyways."),
    General_TeleportConfirm("&cThis teleport is not safe, you will fall for &6%1 &cblocks. Use &6/res tpconfirm &cto perform teleportation anyways."),
    General_TeleportStarted("&eTeleportation to &6%1 &estarted, don't move for next &6%2 &esec."),
    General_TeleportTitle("&eTeleporting!"),
    General_TeleportTitleTime("&6%1"),
    General_TeleportCanceled("&eTeleportation canceled!"),
    General_NoTeleportConfirm("&eThere is no teleports waiting for confirmation!"),
    General_HelpPageHeader2("&eHelp Pages - &6%1 &e- Page <&6%2 &eof &6%3&e>"),
    General_ListExists("&cList already exists..."),
    General_ListRemoved("&eList removed..."),
    General_ListCreate("&eCreated list &6%1"),
    General_PhysicalAreas("&ePhysical Areas"),

    General_CurrentArea("&eCurrent Area: &6%1"),
    General_TotalResSize("&eTotal size: &6%1m\u00B3 (%2m\u00B2)"),
    General_ResSize_eastWest("&eEast/West: &6%1"),
    General_ResSize_northSouth("&eNorth/South: &6%1"),
    General_ResSize_upDown("&eUp/Down: &6%1"),
    General_TotalWorth("&eTotal worth of residence: &6%1 &e(&6%2&e)"),
    General_TotalSubzones("&eSubzones in residence: &6%1 &e(&6%2&e)"),
    General_NotOnline("&eTarget player must be online."),

    General_GenericPages("&ePage &6%1 &eof &6%2 &e(&6%3&e)"),
    General_CoordsTop("&eX:&6%1 &eY:&6%2 &eZ:&6%3"),
    General_CoordsBottom("&eX:&6%1 &eY:&6%2 &eZ:&6%3"),
    General_CoordsLiner("&7 (&3%1&7;%2&7)"),
    General_AllowedTeleportIcon("&2T"),
    General_BlockedTeleportIcon("&7T"),
    General_AllowedMovementIcon("&2M"),
    General_BlockedMovementIcon("&7M"),
    General_AdminToggleTurnOn("&eAutomatic resadmin toggle turned &6On"),
    General_AdminToggleTurnOff("&eAutomatic resadmin toggle turned &6Off"),
    General_NoSpawn("&eYou do not have &6move &epermissions at your spawn point. Relocating"),
    General_CompassTargetReset("&eYour compass has been reset"),
    General_CompassTargetSet("&eYour compass now points to &6%1"),
    General_Ignorelist("&2Ignorelist:&6"),
    General_Blacklist("&cBlacklist:&6"),
    General_LandCost("&eLand cost: &6%1"),
    General_True("&2True"),
    General_False("&cFalse"),
    General_Removed("&6Removed"),
    General_FlagState("&eFlag state: %1"),
    General_Land("&eLand: &6%1"),
    General_Cost("&eCost: &6%1 &eper &6%2 &edays"),
    General_Status("&eStatus: %1"),
    General_Available("&2Available"),
    General_Size(" &eSize: &6%1"),
    General_ResidenceFlags("&eResidence flags: &6%1"),
    General_PlayersFlags("&ePlayers flags: &6%1"),
    General_GroupFlags("&eGroup flags: &6%1"),
    General_OthersFlags("&eOthers flags: &6%1"),
    General_Moved("&eMoved..."),
    General_Name("&eName: &6%1"),
    General_Lists("&eLists: &6"),
    General_Residences("&eResidences&6"),
    General_CreatedOn("&eCreated on: &6%1"),
    General_Owner("&eOwner: &6%1"),
    General_World("&eWorld: &6%1"),
    General_Subzones("&eSubzones"),
    General_NewPlayerInfo(
        "&eIf you want to create protected area for your house, please use wooden axe to select opposite sides of your home and execute command &2/res create YourResidenceName",
        "The below lines represent various messages residence sends to the players.",
        "Note that some messages have variables such as %1 that are inserted at runtime.");

    private Object text;
    private String[] comments;

    private lm(Object text, String... comments) {
        this.text = text;
        this.comments = comments;
    }

    public Object getText() {
        return text;
    }

    public String[] getComments() {
        return comments;
    }

    public String getPath() {
        String path = this.name();
        if (!this.name().contains("Language.") && !this.name().contains("CommandHelp."))
            path = "Language." + this.name();
        return path.replace("_", ".");
    }

    public String getMessage(Object... variables) {
        return Residence.getInstance().getLM().getMessage(this, variables);
    }

    public void sendMessage(CommandSender sender, Object... variables) {

        if (sender == null)
            return;

        if (Residence.getInstance().getLM().containsKey(getPath())) {
            String msg = Residence.getInstance().getLM().getMessage(this, variables);
            if (msg.length() > 0)
                sender.sendMessage(msg);
        } else {
            String msg = getPath();
            if (msg.length() > 0)
                sender.sendMessage(getPath());
        }
    }
}