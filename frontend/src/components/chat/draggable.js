export function makeDraggable(elementId, dragAreaId) {
    const element = document.getElementById(elementId);

    const elementWidth = element.clientWidth;
    // const elementHeight = element.clientHeight;

    const positionMargin = 3;
    const positionMarginBottom = 50;

    let oldEventPositionX = 0;
    let oldEventPositionY = 0;

    if (document.getElementById(dragAreaId)) {
        document.getElementById(dragAreaId).onmousedown = dragMouseDown;
    } else {
        element.onmousedown = dragMouseDown;
    }

    function dragMouseDown(event) {
        event = event || window.event;
        event.preventDefault();
        
        oldEventPositionX = event.clientX;
        oldEventPositionY = event.clientY;

        document.onmouseup = releaseDrag;
        document.onmousemove = elementDrag;
    }

    function limit(min, value, max) {
        return Math.max(min, Math.min(value, max));
    }

    function elementDrag(event) {
        const documentWidth = document.documentElement.clientWidth;
        const documentHeight = document.documentElement.clientHeight;

        event = event || window.event;
        event.preventDefault();

        const maxLeftPosition = documentWidth - elementWidth - positionMargin;
        const maxTopPosition = documentHeight - positionMarginBottom;
        const minLeftPosition = positionMargin;
        const minTopPosition = positionMargin;

        const deltaX = oldEventPositionX - event.clientX;
        const deltaY = oldEventPositionY - event.clientY;

        oldEventPositionX = event.clientX;
        oldEventPositionY = event.clientY;

        const nextLeft = limit(minLeftPosition, element.offsetLeft - deltaX, maxLeftPosition);
        const nextTop = limit(minTopPosition, element.offsetTop - deltaY, maxTopPosition);

        element.style.top = `${nextTop}px`;
        element.style.left = `${nextLeft}px`;
    }

    function releaseDrag() {
        document.onmouseup = null;
        document.onmousemove = null;
    }
}