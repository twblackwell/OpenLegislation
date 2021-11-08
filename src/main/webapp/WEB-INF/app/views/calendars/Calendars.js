import React, { useEffect } from 'react'
import {
    Route,
    Switch,
    useLocation
} from "react-router-dom";

import CalendarSearch from "app/views/calendars/CalendarSearch";
import CalendarUpdates from "app/views/calendars/CalendarUpdates";
import ContentContainer from "app/shared/ContentContainer";

export default function Calendars({ setHeaderText }) {
    const location = useLocation()

    useEffect(() => {
        if (location.pathname === '/calendars/browse') {
            setHeaderText("Browse Calendars")
        }
        if (location.pathname === '/calendars/search') {
            setHeaderText("Search For Calendars")
        }
        if (location.pathname === '/calendars/updates') {
            setHeaderText("View Calendar Updates")
        }
    }, [ location ])

    return (
        <ContentContainer>
            <Switch>
                <Route exact path="/calendars/browse">
                    <div>Work In Progress</div>
                </Route>
                <Route exact path="/calendars/search">
                    <CalendarSearch/>
                </Route>
                <Route exact path="/calendars/updates">
                    <CalendarUpdates/>
                </Route>
            </Switch>
        </ContentContainer>
    )
}